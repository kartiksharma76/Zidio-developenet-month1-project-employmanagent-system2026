package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.entity.Leave;
import com.Comp_Emp_Manage.service.LeaveService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    // Apply for Leave API
    @PostMapping("/apply/{employeeId}")
    public ResponseEntity<?> applyLeave(@PathVariable Long employeeId, @RequestBody LeaveRequest request) {
        try {
            Leave leave = leaveService.applyLeave(
                    employeeId,
                    request.getLeaveType(),
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getReason()
            );
            return ResponseEntity.ok(leave);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Approve/Reject Leave API
    @PutMapping("/status/{leaveId}")
    public ResponseEntity<?> updateLeaveStatus(@PathVariable Long leaveId, @RequestParam String status) {
        try {
            Leave updatedLeave = leaveService.updateLeaveStatus(leaveId, status);
            return ResponseEntity.ok(updatedLeave);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get leaves for a specific employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Leave>> getEmployeeLeaves(@PathVariable Long employeeId) {
        List<Leave> leaves = leaveService.getEmployeeLeaves(employeeId);
        return ResponseEntity.ok(leaves);
    }

    // Get all pending leaves (For managers)
    @GetMapping("/pending")
    public ResponseEntity<List<Leave>> getPendingLeaves() {
        List<Leave> leaves = leaveService.getPendingLeaves();
        return ResponseEntity.ok(leaves);
    }
}

// Simple DTO for Leave Request
@Data
class LeaveRequest {
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
