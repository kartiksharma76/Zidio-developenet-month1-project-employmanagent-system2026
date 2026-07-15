package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.entity.Employee;
import com.Comp_Emp_Manage.entity.Team;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
public class WebTeamController {

    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;

    @GetMapping
    public String viewTeams(Authentication authentication, Model model) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        List<Team> teams = teamRepository.findAll();
        List<Employee> allEmployees = employeeRepository.findAll();

        model.addAttribute("teams", teams);
        model.addAttribute("employees", allEmployees);
        model.addAttribute("isAdminOrManager", isAdminOrManager);
        model.addAttribute("newTeam", new Team());

        return "teams";
    }

    @PostMapping("/add")
    public String createTeam(@ModelAttribute("newTeam") Team team,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdminOrManager) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/teams";
        }

        try {
            if (teamRepository.findByName(team.getName()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Team with name '" + team.getName() + "' already exists.");
                return "redirect:/teams";
            }
            teamRepository.save(team);
            redirectAttributes.addFlashAttribute("success", "Team created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create team: " + e.getMessage());
        }
        return "redirect:/teams";
    }

    @PostMapping("/assign")
    public String assignEmployee(@RequestParam("employeeId") Long employeeId,
                                 @RequestParam("teamId") Long teamId,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdminOrManager) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/teams";
        }

        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
            Team team = teamRepository.findById(teamId)
                    .orElseThrow(() -> new IllegalArgumentException("Team not found"));

            employee.setTeam(team);
            employeeRepository.save(employee);
            redirectAttributes.addFlashAttribute("success", employee.getFirstName() + " assigned to " + team.getName() + " successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Assignment failed: " + e.getMessage());
        }
        return "redirect:/teams";
    }

    @PostMapping("/remove/{employeeId}")
    public String removeEmployee(@PathVariable("employeeId") Long employeeId,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdminOrManager) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/teams";
        }

        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
            employee.setTeam(null);
            employeeRepository.save(employee);
            redirectAttributes.addFlashAttribute("success", "Employee removed from team successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to remove employee: " + e.getMessage());
        }
        return "redirect:/teams";
    }

    @PostMapping("/delete/{id}")
    public String deleteTeam(@PathVariable("id") Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdminOrManager) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/teams";
        }

        try {
            Team team = teamRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Team not found"));
            
            // Unassign all members first
            List<Employee> members = employeeRepository.findAll().stream()
                    .filter(e -> e.getTeam() != null && e.getTeam().getId().equals(id))
                    .toList();
            for (Employee m : members) {
                m.setTeam(null);
                employeeRepository.save(m);
            }

            teamRepository.delete(team);
            redirectAttributes.addFlashAttribute("success", "Team deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete team: " + e.getMessage());
        }
        return "redirect:/teams";
    }
}
