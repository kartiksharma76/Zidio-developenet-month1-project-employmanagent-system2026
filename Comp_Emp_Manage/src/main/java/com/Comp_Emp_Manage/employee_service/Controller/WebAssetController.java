package com.Comp_Emp_Manage.employee_service.Controller;

import com.Comp_Emp_Manage.employee_service.Entity.Asset;
import com.Comp_Emp_Manage.employee_service.Entity.Employee;
import com.Comp_Emp_Manage.employee_service.Repository.AssetRepository;
import com.Comp_Emp_Manage.employee_service.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/assets")
@RequiredArgsConstructor
public class WebAssetController {

    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;

    @GetMapping
    public String viewAssets(Model model, org.springframework.security.core.Authentication auth) {
        boolean isAdminOrManager = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (isAdminOrManager) {
            model.addAttribute("assets", assetRepository.findAll());
            model.addAttribute("employees", employeeRepository.findAll());
            model.addAttribute("newAsset", new Asset());
        } else {
            Employee emp = employeeRepository.findByEmail(auth.getName()).orElse(null);
            if (emp != null) {
                model.addAttribute("assets", assetRepository.findByEmployeeId(emp.getId()));
            }
        }
        
        model.addAttribute("isAdminOrManager", isAdminOrManager);
        return "assets";
    }

    @PostMapping("/add")
    public String addAsset(@ModelAttribute("newAsset") Asset asset, 
                           @RequestParam(value="employeeId", required=false) Long employeeId,
                           RedirectAttributes redirectAttributes) {
        if (employeeId != null) {
            Employee emp = employeeRepository.findById(employeeId).orElse(null);
            asset.setEmployee(emp);
            asset.setStatus(emp != null ? "ASSIGNED" : "AVAILABLE");
        } else {
            asset.setStatus("AVAILABLE");
        }
        
        assetRepository.save(asset);
        redirectAttributes.addFlashAttribute("success", "Asset added successfully!");
        return "redirect:/assets";
    }

    @PostMapping("/unassign/{id}")
    public String unassignAsset(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Asset asset = assetRepository.findById(id).orElse(null);
        if (asset != null) {
            asset.setEmployee(null);
            asset.setStatus("AVAILABLE");
            assetRepository.save(asset);
            redirectAttributes.addFlashAttribute("success", "Asset unassigned successfully!");
        }
        return "redirect:/assets";
    }
}
