package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.entity.CloudinaryImage;
import com.Comp_Emp_Manage.service.CloudinaryService;
import com.Comp_Emp_Manage.repository.CloudinaryImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/documents")
@RequiredArgsConstructor
@Slf4j
public class WebDocumentController {

    private final CloudinaryService cloudinaryService;
    private final CloudinaryImageRepository imageRepository;

    @GetMapping
    public String viewDocuments(Authentication authentication, Model model) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (isAdminOrManager) {
            // Admin and Manager can see ALL documents (filtered from profile pictures)
            List<CloudinaryImage> allDocs = imageRepository.findByRoleScopeStartingWith("DOCUMENT_");
            model.addAttribute("documents", allDocs);
        } else {
            // Employees don't see any lists
            model.addAttribute("documents", List.of());
        }

        model.addAttribute("isAdminOrManager", isAdminOrManager);
        return "documents";
    }

    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("file") MultipartFile file,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Determine role scope for the document upload
            String userRole = authentication.getAuthorities().stream()
                    .map(r -> r.getAuthority())
                    .filter(r -> r.startsWith("ROLE_"))
                    .map(r -> r.replace("ROLE_", ""))
                    .findFirst()
                    .orElse("DOCUMENT");

            // Save reference under DOCUMENT category
            cloudinaryService.uploadImageWithScope(file, "DOCUMENT_" + userRole);
            redirectAttributes.addFlashAttribute("success", "Document uploaded successfully! Only administrators and managers can view uploaded records.");
        } catch (Exception e) {
            log.error("Failed to upload document", e);
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/documents";
    }

    @GetMapping("/download/{id}")
    public void downloadDocument(@PathVariable Long id, jakarta.servlet.http.HttpServletResponse response) {
        try {
            CloudinaryImage image = imageRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Document not found"));

            String url = image.getImageUrl();
            // Force Cloudinary to serve the resource as an attachment (download)
            if (url != null && url.contains("/upload/")) {
                url = url.replace("/upload/", "/upload/fl_attachment/");
            }

            response.sendRedirect(url);
        } catch (Exception e) {
            log.error("Failed to redirect for document download", e);
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteDocument(@PathVariable Long id,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdminOrManager) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/documents";
        }

        try {
            CloudinaryImage image = imageRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Document not found"));
            cloudinaryService.deleteImage(image.getPublicId());
            redirectAttributes.addFlashAttribute("success", "Document deleted successfully.");
        } catch (Exception e) {
            log.error("Failed to delete document", e);
            redirectAttributes.addFlashAttribute("error", "Deletion failed: " + e.getMessage());
        }
        return "redirect:/documents";
    }
}
