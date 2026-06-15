package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.repository.AttendanceRepository;
import com.Comp_Emp_Manage.repository.LeaveRepository;
import com.Comp_Emp_Manage.repository.PayrollRepository;
import com.Comp_Emp_Manage.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WebDashboardController {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    private final PayrollRepository payrollRepository;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isManager = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
        boolean isEmployee = !isAdmin && !isManager;

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isManager", isManager);
        model.addAttribute("isEmployee", isEmployee);

        if (isAdmin || isManager) {
            // Admin and Manager Data
            long totalEmployees = employeeRepository.count();
            long totalLeaves = leaveRepository.count();
            long totalPayrolls = payrollRepository.count();
            
            model.addAttribute("totalEmployees", totalEmployees);
            model.addAttribute("totalLeaves", totalLeaves);
            model.addAttribute("totalPayrolls", totalPayrolls);
        }
        
        if (isEmployee || isManager) {
            // Employee Specific Data (Managers also have their own attendance)
            String email = auth.getName();
            Employee emp = employeeRepository.findByEmail(email).orElse(null);
            
            if (emp != null) {
                model.addAttribute("myAttendanceCount", attendanceRepository.findByEmployeeId(emp.getId()).size());
                model.addAttribute("myLeavesCount", leaveRepository.findByEmployee(emp).size());
                model.addAttribute("myPayslipsCount", payrollRepository.findByEmployeeId(emp.getId()).size());
            }
        }

        return "dashboard";
    }
}
