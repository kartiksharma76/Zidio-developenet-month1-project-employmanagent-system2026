package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.dto.EmployeeRegistrationDto;
import com.Comp_Emp_Manage.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final AuthService authService;

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
        try {
            authService.registerWebEmployee(dto);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
