package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.entity.UserAuth;
import com.Comp_Emp_Manage.entity.Attendance;
import com.Comp_Emp_Manage.entity.Employee;
import com.Comp_Emp_Manage.repository.AttendanceRepository;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class WebAttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceService attendanceService;

    @org.springframework.web.bind.annotation.GetMapping
    public String viewAttendance(Authentication authentication, org.springframework.ui.Model model) {
        String email = authentication.getName();
        Optional<Employee> optEmployee = employeeRepository.findByEmail(email);
        
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));
                
        if (isAdminOrManager) {
            model.addAttribute("attendanceRecords", attendanceRepository.findAll());
        } else if (optEmployee.isPresent()) {
            model.addAttribute("attendanceRecords", attendanceRepository.findByEmployeeId(optEmployee.get().getId()));
        }
        
        model.addAttribute("isAdminOrManager", isAdminOrManager);
        return "attendance";
    }

    @PostMapping("/punch-in")
    public String punchIn(Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Employee> optEmployee = employeeRepository.findByEmail(email);

        if (optEmployee.isPresent()) {
            try {
                Attendance attendance = attendanceService.checkIn(optEmployee.get().getId());
                redirectAttributes.addFlashAttribute("success", "Punched in successfully at " + attendance.getPunchInTimeFormatted());
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee profile not found.");
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/punch-out")
    public String punchOut(Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Employee> optEmployee = employeeRepository.findByEmail(email);

        if (optEmployee.isPresent()) {
            try {
                Attendance attendance = attendanceService.checkOut(optEmployee.get().getId());
                redirectAttributes.addFlashAttribute("success", "Punched out successfully at " + attendance.getPunchOutTimeFormatted());
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee profile not found.");
        }
        return "redirect:/dashboard";
    }
}
