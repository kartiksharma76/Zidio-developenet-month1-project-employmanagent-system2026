package com.Comp_Emp_Manage.employee_service.Controller;

import com.Comp_Emp_Manage.employee_service.Entity.Employee;
import com.Comp_Emp_Manage.employee_service.Entity.Leave;
import com.Comp_Emp_Manage.employee_service.Repository.EmployeeRepository;
import com.Comp_Emp_Manage.employee_service.Repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/leave")
@RequiredArgsConstructor
public class WebLeaveController {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    @GetMapping
    public String viewLeaves(Authentication authentication, Model model) {
        String email = authentication.getName();
        Optional<Employee> optEmployee = employeeRepository.findByEmail(email);
        
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (isAdminOrManager) {
            model.addAttribute("leaves", leaveRepository.findAll());
        } else if (optEmployee.isPresent()) {
            Employee employee = optEmployee.get();
            List<Leave> myLeaves = leaveRepository.findByEmployee(employee);
            model.addAttribute("leaves", myLeaves);
        } else {
            model.addAttribute("leaves", List.of());
        }
        
        model.addAttribute("isAdminOrManager", isAdminOrManager);
        model.addAttribute("newLeave", new LeaveRequestDto());
        return "leave";
    }

    @PostMapping("/apply")
    public String applyLeave(@ModelAttribute("newLeave") LeaveRequestDto dto, Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Employee> optEmployee = employeeRepository.findByEmail(email);

        if (optEmployee.isPresent()) {
            Employee employee = optEmployee.get();
            Leave leave = Leave.builder()
                    .employee(employee)
                    .leaveType(dto.getLeaveType())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .reason(dto.getReason())
                    .status("PENDING")
                    .build();
            leaveRepository.save(leave);
            redirectAttributes.addFlashAttribute("success", "Leave request submitted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error applying for leave. Employee not found.");
        }
        return "redirect:/leave";
    }
    @PostMapping("/approve/{id}")
    public String approveLeave(@org.springframework.web.bind.annotation.PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (isAdminOrManager) {
            Optional<Leave> leaveOpt = leaveRepository.findById(id);
            if (leaveOpt.isPresent()) {
                Leave leave = leaveOpt.get();
                leave.setStatus("APPROVED");
                leaveRepository.save(leave);
                redirectAttributes.addFlashAttribute("success", "Leave approved successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Leave request not found.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
        }
        return "redirect:/leave";
    }

    @PostMapping("/reject/{id}")
    public String rejectLeave(@org.springframework.web.bind.annotation.PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (isAdminOrManager) {
            Optional<Leave> leaveOpt = leaveRepository.findById(id);
            if (leaveOpt.isPresent()) {
                Leave leave = leaveOpt.get();
                leave.setStatus("REJECTED");
                leaveRepository.save(leave);
                redirectAttributes.addFlashAttribute("success", "Leave rejected successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Leave request not found.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
        }
        return "redirect:/leave";
    }
}
