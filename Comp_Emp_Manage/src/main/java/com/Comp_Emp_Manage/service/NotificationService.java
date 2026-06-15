package com.Comp_Emp_Manage.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.entity.Employee;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final EmployeeRepository employeeRepository;

    // Send real-time notification over WebSocket
    public void sendLiveNotification(String topic, String message) {
        messagingTemplate.convertAndSend("/topic/" + topic, message);
    }

    // Mock Email Notification to Registered Employee
    public void sendEmailToEmployee(Long employeeId, String subject, String body) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String to = employee.getEmail();
        log.info("Mock Email Sent to Registered Email: {}", to);
        log.info("Subject: {}", subject);
        log.info("Body: {}", body);
    }

    // Mock SMS Notification to Registered Employee
    public void sendSmsToEmployee(Long employeeId, String message) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        String phoneNumber = employee.getPhone(); // Assuming Employee has a getPhone() method
        log.info("Mock SMS Sent to Registered Phone: {}", phoneNumber);
        log.info("Message: {}", message);
    }
}
