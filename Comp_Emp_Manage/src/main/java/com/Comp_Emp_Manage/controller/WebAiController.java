package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.repository.LeaveRepository;
import com.Comp_Emp_Manage.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/ai-insights")
@RequiredArgsConstructor
public class WebAiController {

    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;
    private final AiService aiService;

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
        
        String reply = aiService.chatWithAi(userMessage, totalEmployees, totalLeaves, pendingLeaves);
        return ResponseEntity.ok(Collections.singletonMap("reply", reply));
    }
}
