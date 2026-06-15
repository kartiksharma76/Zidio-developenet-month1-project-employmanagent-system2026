package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.entity.Payroll;
import com.Comp_Emp_Manage.service.PayrollService;
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
    public ResponseEntity<Payroll> generatePayroll(@PathVariable Long employeeId, @RequestParam(defaultValue = "0.0") Double allowances) {
        Payroll payroll = payrollService.generatePayroll(employeeId, allowances);
        return ResponseEntity.ok(payroll);
    }

    @PutMapping("/pay/{payrollId}")
    public ResponseEntity<Payroll> markAsPaid(@PathVariable Long payrollId) {
        Payroll payroll = payrollService.markAsPaid(payrollId);
        return ResponseEntity.ok(payroll);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Payroll>> getEmployeePayroll(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payrollService.getEmployeePayroll(employeeId));
    }
}
