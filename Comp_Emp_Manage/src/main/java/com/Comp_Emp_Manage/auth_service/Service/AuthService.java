package com.Comp_Emp_Manage.auth_service.Service;

import com.Comp_Emp_Manage.auth_service.Dto.AuthRequest;
import com.Comp_Emp_Manage.auth_service.Dto.AuthResponse;
import com.Comp_Emp_Manage.auth_service.Dto.SignupRequest;
import com.Comp_Emp_Manage.auth_service.Entity.Role;
import com.Comp_Emp_Manage.auth_service.Entity.UserAuth;
import com.Comp_Emp_Manage.auth_service.Repository.UserAuthRepository;
import com.Comp_Emp_Manage.auth_service.Security.JwtTokenProvider;
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
}
