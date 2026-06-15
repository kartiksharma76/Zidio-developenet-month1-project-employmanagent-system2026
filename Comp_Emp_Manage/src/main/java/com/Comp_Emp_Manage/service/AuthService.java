package com.Comp_Emp_Manage.service;

import com.Comp_Emp_Manage.dto.AuthRequest;
import com.Comp_Emp_Manage.dto.AuthResponse;
import com.Comp_Emp_Manage.dto.SignupRequest;
import com.Comp_Emp_Manage.enums.Role;
import com.Comp_Emp_Manage.entity.UserAuth;
import com.Comp_Emp_Manage.repository.UserAuthRepository;
import com.Comp_Emp_Manage.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final com.Comp_Emp_Manage.repository.EmployeeRepository employeeRepository;

    public String registerUser(SignupRequest signupRequest) {
        if (userAuthRepository.existsByUserEmail(signupRequest.getUserEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        String roleName = signupRequest.getRole().toUpperCase();
        if (!roleName.startsWith("ROLE_")) roleName = "ROLE_" + roleName;

        UserAuth user = UserAuth.builder()
                .userName(signupRequest.getUserName())
                .userEmail(signupRequest.getUserEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(Role.valueOf(roleName))
                .build();

        userAuthRepository.save(user);

        return "User registered successfully!";
    }

    public AuthResponse authenticateUser(AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        
        // Simulating a refresh token (can be implemented properly with a separate entity later if needed)
        String refreshToken = jwt + "-refresh"; 

        UserAuth user = userAuthRepository.findByUserEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .email(user.getUserEmail())
                .role(user.getRole().name())
                .build();
    }

    public void registerWebEmployee(com.Comp_Emp_Manage.dto.EmployeeRegistrationDto dto) {
        if (userAuthRepository.existsByUserEmail(dto.getEmail())) {
            throw new RuntimeException("Email is already registered.");
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

        com.Comp_Emp_Manage.entity.Employee employee = com.Comp_Emp_Manage.entity.Employee.builder()
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .email(dto.getEmail())
            .phone(dto.getPhone() != null ? dto.getPhone() : "N/A")
            .department(dto.getDepartment())
            .userAuth(userAuth)
            .build();
        
        employeeRepository.save(employee);
    }
}
