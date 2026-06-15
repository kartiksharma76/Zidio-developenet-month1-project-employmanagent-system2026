package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.entity.Employee;
import com.Comp_Emp_Manage.entity.Performance;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/performance")
@RequiredArgsConstructor
public class WebPerformanceController {

    private final PerformanceRepository performanceRepository;
    private final EmployeeRepository employeeRepository;

    @GetMapping
    public String viewPerformance(org.springframework.security.core.Authentication authentication, Model model) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (isAdminOrManager) {
            model.addAttribute("reviews", performanceRepository.findAll());
            model.addAttribute("employees", employeeRepository.findAll());
        } else {
            String email = authentication.getName();
            Employee employee = employeeRepository.findByEmail(email).orElse(null);
            if (employee != null) {
                model.addAttribute("reviews", performanceRepository.findByEmployeeId(employee.getId()));
            } else {
                model.addAttribute("reviews", java.util.List.of());
            }
            // Employees don't need the full employee list to add reviews
        }
        model.addAttribute("isAdminOrManager", isAdminOrManager);
        return "performance";
    }

    @PostMapping("/add")
    public String addReview(@RequestParam Long employeeId,
                            @RequestParam Integer rating,
                            @RequestParam String goals,
                            @RequestParam String feedback,
                            RedirectAttributes redirectAttributes) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            Performance performance = Performance.builder()
                    .employee(employee)
                    .reviewDate(LocalDate.now())
                    .rating(rating)
                    .goals(goals)
                    .feedback(feedback)
                    .reviewerName("HR Manager") // Hardcoded for now
                    .build();

            performanceRepository.save(performance);
            redirectAttributes.addFlashAttribute("success", "Performance review saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving review: " + e.getMessage());
        }
        return "redirect:/performance";
    }
}
