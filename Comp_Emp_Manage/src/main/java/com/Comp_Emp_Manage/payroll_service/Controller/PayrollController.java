package com.Comp_Emp_Manage.payroll_service.Controller;

import com.Comp_Emp_Manage.payroll_service.Entity.Payroll;
import com.Comp_Emp_Manage.payroll_service.Service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping("/generate/{employeeId}")
    public ResponseEntity<?> generatePayroll(@PathVariable Long employeeId, @RequestParam(defaultValue = "0.0") Double allowances) {
        try {
            Payroll payroll = payrollService.generatePayroll(employeeId, allowances);
            return ResponseEntity.ok(payroll);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/pay/{payrollId}")
    public ResponseEntity<?> markAsPaid(@PathVariable Long payrollId) {
        try {
            Payroll payroll = payrollService.markAsPaid(payrollId);
            return ResponseEntity.ok(payroll);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Payroll>> getEmployeePayroll(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payrollService.getEmployeePayroll(employeeId));
    }
}
