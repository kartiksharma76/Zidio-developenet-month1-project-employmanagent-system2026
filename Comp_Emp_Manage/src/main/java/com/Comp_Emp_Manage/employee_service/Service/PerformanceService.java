package com.Comp_Emp_Manage.employee_service.Service;

import com.Comp_Emp_Manage.employee_service.Entity.Performance;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;

@Service
public class PerformanceService {

    public Performance addPerformanceReview(Long employeeId, String goals, Integer rating, String feedback) {
        // TODO: Implement actual logic with Repository
        return new Performance();
    }

    public List<Performance> getEmployeePerformance(Long employeeId) {
        return Collections.emptyList();
    }
}
