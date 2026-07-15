package com.Comp_Emp_Manage.manage;

import com.Comp_Emp_Manage.entity.CloudinaryImage;
import com.Comp_Emp_Manage.repository.CloudinaryImageRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final CloudinaryImageRepository cloudinaryImageRepository;

    @Autowired
    public GlobalControllerAdvice(CloudinaryImageRepository cloudinaryImageRepository) {
        this.cloudinaryImageRepository = cloudinaryImageRepository;
    }

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("bodyClass")
    public String bodyClass(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri == null) return "";
        if (uri.startsWith("/dashboard")) return "bg-dashboard";
        if (uri.startsWith("/profile")) return "bg-profile";
        if (uri.startsWith("/documents")) return "bg-documents";
        if (uri.startsWith("/employees")) return "bg-employees";
        if (uri.startsWith("/announcements")) return "bg-announcements";
        if (uri.startsWith("/assets")) return "bg-assets";
        if (uri.startsWith("/expenses")) return "bg-expenses";
        if (uri.startsWith("/attendance")) return "bg-attendance";
        if (uri.startsWith("/leave")) return "bg-leave";
        if (uri.startsWith("/payroll")) return "bg-payroll";
        if (uri.startsWith("/performance")) return "bg-performance";
        if (uri.startsWith("/reports")) return "bg-reports";
        if (uri.startsWith("/ai-insights")) return "bg-ai-insights";
        if (uri.startsWith("/teams")) return "bg-teams";
        return "";
    }

    @ModelAttribute("globalProfileImageUrl")
    public String globalProfileImageUrl() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                String email = auth.getName();
                if (cloudinaryImageRepository != null) {
                    var images = cloudinaryImageRepository.findByUploadedByUsername(email);
                    if (images != null && !images.isEmpty()) {
                        // Find latest image that is not raw (i.e. is an image)
                        for (int i = images.size() - 1; i >= 0; i--) {
                            var img = images.get(i);
                            if (img != null && img.getImageUrl() != null) {
                                String url = img.getImageUrl().toLowerCase();
                                if (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".webp")) {
                                    return img.getImageUrl();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading global profile image: " + e.getMessage());
        }
        return null;
    }
}
