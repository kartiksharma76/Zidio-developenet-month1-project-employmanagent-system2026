package com.Comp_Emp_Manage.service;

import com.Comp_Emp_Manage.entity.Performance;
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
