package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.entity.UserAuth;
import com.Comp_Emp_Manage.entity.Attendance;
import com.Comp_Emp_Manage.entity.Employee;
import com.Comp_Emp_Manage.repository.AttendanceRepository;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
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
            Employee employee = optEmployee.get();
            LocalDate today = LocalDate.now();
            
            Optional<Attendance> optAttendance = attendanceRepository.findByEmployeeAndDate(employee, today);
            
            if (optAttendance.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "You have already punched in today.");
            } else {
                Attendance attendance = Attendance.builder()
                        .employee(employee)
                        .date(today)
                        .punchInTime(LocalTime.now())
                        .status("PRESENT")
                        .build();
                attendanceRepository.save(attendance);
                redirectAttributes.addFlashAttribute("success", "Punched in successfully at " + LocalTime.now());
            }
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/punch-out")
    public String punchOut(Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Employee> optEmployee = employeeRepository.findByEmail(email);

        if (optEmployee.isPresent()) {
            Employee employee = optEmployee.get();
            LocalDate today = LocalDate.now();
            
            Optional<Attendance> optAttendance = attendanceRepository.findByEmployeeAndDate(employee, today);
            
            if (optAttendance.isPresent()) {
                Attendance attendance = optAttendance.get();
                if (attendance.getPunchOutTime() != null) {
                    redirectAttributes.addFlashAttribute("error", "You have already punched out today.");
                } else {
                    attendance.setPunchOutTime(LocalTime.now());
                    attendanceRepository.save(attendance);
                    redirectAttributes.addFlashAttribute("success", "Punched out successfully at " + LocalTime.now());
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "You must punch in first.");
            }
        }
        return "redirect:/dashboard";
    }
}
