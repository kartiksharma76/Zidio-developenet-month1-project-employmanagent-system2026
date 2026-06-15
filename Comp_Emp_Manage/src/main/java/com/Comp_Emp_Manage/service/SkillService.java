package com.Comp_Emp_Manage.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SkillService {

    public List<String> calculateSkillGap(Long employeeId) {
        // Mocking skill gap analysis logic
        // In a real system, this would query the employee's current skills from DB
        // and compare them with the required skills for their role.
        
        List<String> requiredSkills = Arrays.asList("Java", "Spring Boot", "React", "Docker", "Kubernetes");
        
        // Simulating the gap based on employeeId
        if (employeeId % 2 == 0) {
            return Arrays.asList("React", "Docker");
        } else {
            return Arrays.asList("Kubernetes", "AWS");
        }
    }
}
