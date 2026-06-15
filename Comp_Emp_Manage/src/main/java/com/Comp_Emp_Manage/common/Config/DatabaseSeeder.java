package com.Comp_Emp_Manage.common.Config;

import com.Comp_Emp_Manage.auth_service.Entity.Role;
import com.Comp_Emp_Manage.auth_service.Entity.UserAuth;
import com.Comp_Emp_Manage.auth_service.Repository.UserAuthRepository;
import com.Comp_Emp_Manage.employee_service.Entity.Employee;
import com.Comp_Emp_Manage.employee_service.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder {

    private final EmployeeRepository employeeRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            // Seed Admin User
            if (!employeeRepository.existsByEmail("admin@nexushr.com")) {
                UserAuth adminAuth = UserAuth.builder()
                        .userName("admin")
                        .userEmail("admin@nexushr.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ROLE_ADMIN)
                        .build();

                Employee admin = new Employee();
                admin.setFirstName("Super");
                admin.setLastName("Admin");
                admin.setEmail("admin@nexushr.com");
                admin.setPhone("1234567890");
                admin.setDepartment("Management");
                admin.setDesignation("System Administrator");
                admin.setJoiningDate(LocalDate.now());
                admin.setSalary(150000.0);
                admin.setUserAuth(adminAuth);
                
                employeeRepository.save(admin);
            }

            // Seed Mock Employee
            if (!employeeRepository.existsByEmail("john.doe@nexushr.com")) {
                UserAuth empAuth = UserAuth.builder()
                        .userName("john")
                        .userEmail("john.doe@nexushr.com")
                        .password(passwordEncoder.encode("password123"))
                        .role(Role.ROLE_EMPLOYEE)
                        .build();

                Employee emp = new Employee();
                emp.setFirstName("John");
                emp.setLastName("Doe");
                emp.setEmail("john.doe@nexushr.com");
                emp.setPhone("9876543210");
                emp.setDepartment("Engineering");
                emp.setDesignation("Software Engineer");
                emp.setJoiningDate(LocalDate.now());
                emp.setSalary(75000.0);
                emp.setUserAuth(empAuth);
                
                employeeRepository.save(emp);
            }
        };
    }
}
