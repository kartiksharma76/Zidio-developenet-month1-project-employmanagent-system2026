package com.Comp_Emp_Manage.employee_service.Controller;

import lombok.Data;

@Data
public class EmployeeRegistrationDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String department;
    private String phone;
    private String role;
    private Double salary;
}
