package com.Comp_Emp_Manage.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String userName;
    private String userEmail;
    private String password;
    private String role; // e.g., "ROLE_EMPLOYEE", "ROLE_ADMIN"
}