package com.Comp_Emp_Manage.common.Controller;

import com.Comp_Emp_Manage.common.Service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/attrition/{employeeId}")
    public ResponseEntity<String> predictAttrition(@PathVariable Long employeeId) {
        String prediction = aiService.predictAttrition(employeeId);
        return ResponseEntity.ok(prediction);
    }
}
