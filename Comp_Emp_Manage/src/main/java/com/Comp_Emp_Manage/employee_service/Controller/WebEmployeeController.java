package com.Comp_Emp_Manage.employee_service.Controller;

import com.Comp_Emp_Manage.employee_service.Entity.Employee;
import com.Comp_Emp_Manage.auth_service.Entity.UserAuth;
import com.Comp_Emp_Manage.auth_service.Entity.Role;
import com.Comp_Emp_Manage.employee_service.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebEmployeeController {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/employees")
    public String viewEmployees(Model model, @RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("employees", employeeRepository.findAll().stream()
                .filter(e -> e.getFirstName().toLowerCase().contains(search.toLowerCase()) || 
                             e.getLastName().toLowerCase().contains(search.toLowerCase()) || 
                             e.getDepartment().toLowerCase().contains(search.toLowerCase()))
                .toList());
            model.addAttribute("searchQuery", search);
        } else {
            model.addAttribute("employees", employeeRepository.findAll());
        }
        model.addAttribute("newEmployee", new EmployeeRegistrationDto());
        return "employees";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, org.springframework.security.core.Authentication auth) {
        String email = auth.getName();
        Employee employee = employeeRepository.findByEmail(email).orElse(null);
        if (employee != null) {
            model.addAttribute("employee", employee);
        }
        return "profile";
    }

    @PostMapping("/employees/add")
    public String addEmployee(@ModelAttribute("newEmployee") EmployeeRegistrationDto dto, RedirectAttributes redirectAttributes) {
        UserAuth userAuth = UserAuth.builder()
            .userName(dto.getFirstName() + " " + dto.getLastName())
            .userEmail(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .role(Role.ROLE_EMPLOYEE)
            .build();

        Employee employee = Employee.builder()
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .email(dto.getEmail())
            .phone(dto.getPhone() != null ? dto.getPhone() : "N/A")
            .department(dto.getDepartment())
            .salary(dto.getSalary())
            .userAuth(userAuth)
            .build();

        employeeRepository.save(employee);
        redirectAttributes.addFlashAttribute("success", "Employee added successfully!");
        return "redirect:/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            redirectAttributes.addFlashAttribute("error", "Employee not found");
            return "redirect:/employees";
        }
        model.addAttribute("employee", employee);
        return "edit-employee";
    }

    @PostMapping("/employees/edit/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute("employee") Employee updatedEmployee, RedirectAttributes redirectAttributes) {
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if (existingEmployee != null) {
            existingEmployee.setFirstName(updatedEmployee.getFirstName());
            existingEmployee.setLastName(updatedEmployee.getLastName());
            existingEmployee.setEmail(updatedEmployee.getEmail());
            existingEmployee.setPhone(updatedEmployee.getPhone() != null ? updatedEmployee.getPhone() : "N/A");
            existingEmployee.setDepartment(updatedEmployee.getDepartment());
            existingEmployee.setSalary(updatedEmployee.getSalary());
            
            employeeRepository.save(existingEmployee);
            redirectAttributes.addFlashAttribute("success", "Employee updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee not found");
        }
        return "redirect:/employees";
    }

    @PostMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Employee deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee not found");
        }
        return "redirect:/employees";
    }
}
