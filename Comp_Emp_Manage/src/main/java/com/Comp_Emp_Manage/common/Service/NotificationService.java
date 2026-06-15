package com.Comp_Emp_Manage.common.Service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.Comp_Emp_Manage.employee_service.Repository.EmployeeRepository; // Corrected import
import com.Comp_Emp_Manage.employee_service.Entity.Employee; // Corrected import

@Service
@RequiredArgsConstructor
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
        System.out.println("Mock Email Sent to Registered Email: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }

    // Mock SMS Notification to Registered Employee
    public void sendSmsToEmployee(Long employeeId, String message) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        String phoneNumber = employee.getPhone(); // Assuming Employee has a getPhone() method
        System.out.println("Mock SMS Sent to Registered Phone: " + phoneNumber);
        System.out.println("Message: " + message);
    }
}
