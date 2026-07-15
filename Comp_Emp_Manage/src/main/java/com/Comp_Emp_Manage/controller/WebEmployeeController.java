package com.Comp_Emp_Manage.controller;
import com.Comp_Emp_Manage.dto.EmployeeRegistrationDto;


import com.Comp_Emp_Manage.entity.Employee;
import com.Comp_Emp_Manage.entity.UserAuth;
import com.Comp_Emp_Manage.entity.CloudinaryImage;
import com.Comp_Emp_Manage.enums.Role;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.repository.CloudinaryImageRepository;
import com.Comp_Emp_Manage.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebEmployeeController {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final CloudinaryImageRepository cloudinaryImageRepository;

    @GetMapping("/employees")
    public String viewEmployees(Model model, @RequestParam(value = "search", required = false) String search) {
        var allEmployees = employeeRepository.findAll();
        if (search != null && !search.trim().isEmpty()) {
            var filtered = allEmployees.stream()
                .filter(e -> e.getFirstName().toLowerCase().contains(search.toLowerCase()) || 
                             e.getLastName().toLowerCase().contains(search.toLowerCase()) || 
                             e.getDepartment().toLowerCase().contains(search.toLowerCase()))
                .toList();
            model.addAttribute("employees", filtered);
            model.addAttribute("searchQuery", search);
            model.addAttribute("freshers", filtered.stream().filter(e -> Boolean.TRUE.equals(e.getFresher())).toList());
        } else {
            model.addAttribute("employees", allEmployees);
            model.addAttribute("freshers", allEmployees.stream().filter(e -> Boolean.TRUE.equals(e.getFresher())).toList());
        }
        long totalFreshers = allEmployees.stream().filter(e -> Boolean.TRUE.equals(e.getFresher())).count();
        model.addAttribute("totalFreshers", totalFreshers);
        model.addAttribute("totalEmployeesCount", allEmployees.size());
        model.addAttribute("newEmployee", new EmployeeRegistrationDto());
        return "employees";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, org.springframework.security.core.Authentication auth) {
        String email = auth.getName();
        Employee employee = employeeRepository.findByEmail(email).orElse(null);
        if (employee != null) {
            model.addAttribute("employee", employee);
            
            // Fetch uploaded profile pictures for this user (take the latest one that is a valid image format)
            var images = cloudinaryImageRepository.findByUploadedByUsername(email);
            if (!images.isEmpty()) {
                CloudinaryImage latestImage = null;
                for (int i = images.size() - 1; i >= 0; i--) {
                    var img = images.get(i);
                    if (img != null && img.getImageUrl() != null && (img.getRoleScope() == null || !img.getRoleScope().startsWith("DOCUMENT_"))) {
                        String url = img.getImageUrl().toLowerCase();
                        if (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".webp")) {
                            latestImage = img;
                            break;
                        }
                    }
                }
                if (latestImage != null) {
                    model.addAttribute("profileImageUrl", latestImage.getImageUrl());
                    model.addAttribute("profileImagePublicId", latestImage.getPublicId());
                    model.addAttribute("profileImageFilename", latestImage.getOriginalFilename());
                }
            }
        }
        return "profile";
    }

    @PostMapping("/profile/upload")
    public String uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            org.springframework.security.core.Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {
            String contentType = file.getContentType();
            String filename = file.getOriginalFilename();
            boolean isImage = false;
            
            if (contentType != null) {
                isImage = contentType.startsWith("image/");
            } else if (filename != null) {
                String nameLower = filename.toLowerCase();
                isImage = nameLower.endsWith(".jpg") || nameLower.endsWith(".jpeg") || nameLower.endsWith(".png") || nameLower.endsWith(".webp");
            }
            
            if (!isImage) {
                redirectAttributes.addFlashAttribute("error", "Only image files (JPG, PNG, WEBP) are allowed as profile pictures!");
                return "redirect:/profile";
            }

            // Determine uploader role scope based on logged-in user authority
            String roleScope = auth.getAuthorities().stream()
                    .map(r -> r.getAuthority())
                    .filter(r -> r.startsWith("ROLE_"))
                    .map(r -> r.replace("ROLE_", ""))
                    .findFirst()
                    .orElse("PUBLIC");

            cloudinaryService.uploadImageWithScope(file, roleScope);
            redirectAttributes.addFlashAttribute("success", "Profile picture uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/delete")
    public String deleteProfilePicture(
            @RequestParam("publicId") String publicId,
            RedirectAttributes redirectAttributes) {
        try {
            cloudinaryService.deleteImage(publicId);
            redirectAttributes.addFlashAttribute("success", "Profile picture deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Deletion failed: " + e.getMessage());
        }
        return "redirect:/profile";
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
            .fresher(dto.getFresher() != null ? dto.getFresher() : false)
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
            existingEmployee.setFresher(updatedEmployee.getFresher() != null ? updatedEmployee.getFresher() : false);
            
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
