package com.Comp_Emp_Manage.employee_service.Controller;

import com.Comp_Emp_Manage.employee_service.Entity.Employee;
import com.Comp_Emp_Manage.employee_service.Repository.EmployeeRepository;
import com.Comp_Emp_Manage.employee_service.Repository.LeaveRepository;
import com.Comp_Emp_Manage.employee_service.Entity.Performance;
import com.Comp_Emp_Manage.employee_service.Repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;

@Controller
@RequestMapping("/ai-insights")
@RequiredArgsConstructor
public class WebAiController {

    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;
    private final PerformanceRepository performanceRepository;

    @Value("${nvidia.api.key}")
    private String nvidiaApiKey;

    @GetMapping
    public String viewInsights(Model model) {
        return "ai_insights";
    }

    @PostMapping("/chat")
    @ResponseBody
    public ResponseEntity<Map<String, String>> chatWithAi(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Message cannot be empty"));
        }

        // Gather real-time context from the database
        long totalEmployees = employeeRepository.count();
        long totalLeaves = leaveRepository.count();
        long pendingLeaves = leaveRepository.findAll().stream().filter(l -> "PENDING".equalsIgnoreCase(l.getStatus())).count();
        
        String systemPrompt = "You are NexusHR AI, an intelligent Human Resources assistant. "
                + "You have access to the following real-time company statistics: "
                + "Total Employees: " + totalEmployees + ", "
                + "Total Leave Requests: " + totalLeaves + " (" + pendingLeaves + " pending). "
                + "Answer the user's questions about HR, company policies, or the provided statistics concisely and professionally. "
                + "If they ask something completely unrelated to HR or the company, politely steer the conversation back to HR topics.";

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(nvidiaApiKey);

            String requestBody = "{\n" +
                    "  \"model\": \"meta/llama-3.1-70b-instruct\",\n" +
                    "  \"messages\": [\n" +
                    "    {\"role\": \"system\", \"content\": \"" + systemPrompt.replace("\"", "\\\"") + "\"},\n" +
                    "    {\"role\": \"user\", \"content\": \"" + userMessage.replace("\"", "\\\"").replace("\n", " ") + "\"}\n" +
                    "  ],\n" +
                    "  \"max_tokens\": 512,\n" +
                    "  \"temperature\": 0.5\n" +
                    "}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://integrate.api.nvidia.com/v1/chat/completions",
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
                    String reply = (String) messageObj.get("content");
                    return ResponseEntity.ok(Collections.singletonMap("reply", reply));
                }
            }
            return ResponseEntity.ok(Collections.singletonMap("reply", "I'm sorry, I couldn't generate a response at the moment."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.singletonMap("reply", "Error communicating with the AI service: " + e.getMessage()));
        }
    }
}
