package com.Comp_Emp_Manage.common.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    @Value("${nvidia.api.key}")
    private String apiKey;

    public String predictAttrition(Long employeeId) {
        // Integration logic with NVIDIA NIM or OpenAI
        // Mocking response for NexusHR prototype
        double risk = Math.random() * 100;
        return "Attrition Risk for ID " + employeeId + " is " + String.format("%.2f", risk) + "%";
    }
}