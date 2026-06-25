package com.Comp_Emp_Manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username:noreply@example.com}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        if (javaMailSender == null) {
            System.out.println("Mock OTP Email sent successfully to " + toEmail + " with OTP: " + otp);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Your Password Reset OTP");
            message.setText("Hello,\n\nYour OTP for password reset is: " + otp + "\n\nThis OTP will expire in 5 minutes.\n\nRegards,\nNexusHR Team");

            javaMailSender.send(message);
            System.out.println("OTP Email sent successfully to " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            throw new RuntimeException("Failed to send email. Please check your email configuration.");
        }
    }
}
