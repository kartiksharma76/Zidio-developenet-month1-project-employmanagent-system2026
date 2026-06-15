package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping("/gap/{employeeId}")
    public ResponseEntity<List<String>> getSkillGap(@PathVariable Long employeeId) {
        List<String> missingSkills = skillService.calculateSkillGap(employeeId);
        return ResponseEntity.ok(missingSkills);
    }
}
