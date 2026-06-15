package com.Comp_Emp_Manage.auth_service.Controller;

import com.Comp_Emp_Manage.employee_service.Entity.Employee;
import com.Comp_Emp_Manage.auth_service.Entity.UserAuth;
import com.Comp_Emp_Manage.auth_service.Entity.Role;
import com.Comp_Emp_Manage.employee_service.Repository.EmployeeRepository;
import com.Comp_Emp_Manage.employee_service.Controller.EmployeeRegistrationDto;
import com.Comp_Emp_Manage.auth_service.Repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final EmployeeRepository employeeRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("employee", new EmployeeRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerEmployee(@ModelAttribute("employee") EmployeeRegistrationDto dto, RedirectAttributes redirectAttributes) {
        if (employeeRepository.existsByEmail(dto.getEmail()) || userAuthRepository.existsByUserEmail(dto.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email is already registered.");
            return "redirect:/register";
        }

        Role userRole = Role.ROLE_EMPLOYEE;
        if (dto.getRole() != null) {
            try {
                userRole = Role.valueOf(dto.getRole());
            } catch (IllegalArgumentException e) {
                userRole = Role.ROLE_EMPLOYEE;
            }
        }

        UserAuth userAuth = UserAuth.builder()
            .userName(dto.getFirstName() + " " + dto.getLastName())
            .userEmail(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .role(userRole)
            .build();

        Employee employee = Employee.builder()
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .email(dto.getEmail())
            .phone(dto.getPhone() != null ? dto.getPhone() : "N/A")
            .department(dto.getDepartment())
            .userAuth(userAuth)
            .build();
        
        employeeRepository.save(employee);
        
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }
}
