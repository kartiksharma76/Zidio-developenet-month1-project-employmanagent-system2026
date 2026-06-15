package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class ApiDashboardController {

    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Count employees per department
        Map<String, Long> departmentCounts = new HashMap<>();
        employeeRepository.findAll().forEach(emp -> {
            String dept = emp.getDepartment() != null ? emp.getDepartment() : "Unassigned";
            departmentCounts.put(dept, departmentCounts.getOrDefault(dept, 0L) + 1);
        });
        
        // Count total leaves vs pending leaves
        long totalLeaves = leaveRepository.count();
        long pendingLeaves = leaveRepository.findAll().stream()
                .filter(l -> "PENDING".equalsIgnoreCase(l.getStatus()))
                .count();

        stats.put("departmentDistribution", departmentCounts);
        stats.put("totalLeaves", totalLeaves);
        stats.put("pendingLeaves", pendingLeaves);
        
        return ResponseEntity.ok(stats);
    }
}
