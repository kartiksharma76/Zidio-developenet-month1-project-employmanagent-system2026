package com.Comp_Emp_Manage.employee_service.Controller;

import com.Comp_Emp_Manage.employee_service.Entity.Performance;
import com.Comp_Emp_Manage.employee_service.Service.PerformanceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping("/add/{employeeId}")
    public ResponseEntity<?> addReview(@PathVariable Long employeeId, @RequestBody PerformanceRequest request) {
        try {
            Performance review = performanceService.addPerformanceReview(
                    employeeId, request.getGoals(), request.getRating(), request.getFeedback()
            );
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Performance>> getEmployeePerformance(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceService.getEmployeePerformance(employeeId));
    }
}

@Data
class PerformanceRequest {
    private String goals;
    private Integer rating;
    private String feedback;
}
