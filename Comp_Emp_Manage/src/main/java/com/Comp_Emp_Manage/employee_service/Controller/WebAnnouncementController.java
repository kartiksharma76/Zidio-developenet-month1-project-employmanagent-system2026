package com.Comp_Emp_Manage.employee_service.Controller;

import com.Comp_Emp_Manage.employee_service.Entity.Announcement;
import com.Comp_Emp_Manage.employee_service.Repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class WebAnnouncementController {

    private final AnnouncementRepository announcementRepository;

    @GetMapping
    public String viewAnnouncements(Model model) {
        model.addAttribute("announcements", announcementRepository.findAllByOrderByDatePostedDesc());
        model.addAttribute("newAnnouncement", new Announcement());
        return "announcements";
    }

    @PostMapping("/add")
    public String addAnnouncement(@ModelAttribute("newAnnouncement") Announcement announcement, 
                                  org.springframework.security.core.Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        announcement.setAuthorName(auth.getName());
        announcement.setDatePosted(LocalDateTime.now());
        announcementRepository.save(announcement);
        redirectAttributes.addFlashAttribute("success", "Announcement posted successfully!");
        return "redirect:/announcements";
    }

    @PostMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (announcementRepository.existsById(id)) {
            announcementRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Announcement deleted successfully!");
        }
        return "redirect:/announcements";
    }
}
