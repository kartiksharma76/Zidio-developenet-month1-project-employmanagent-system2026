package com.Comp_Emp_Manage.payroll_service.Service;

import com.Comp_Emp_Manage.payroll_service.Entity.Payroll;
import com.Comp_Emp_Manage.payroll_service.Repository.PayrollRepository;
import com.Comp_Emp_Manage.employee_service.Entity.Employee;
import com.Comp_Emp_Manage.employee_service.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    public Payroll generatePayroll(Long employeeId, Double allowances) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Double basicSalary = employee.getSalary() != null ? employee.getSalary() : 0.0;
        // Basic calculation logic
        Double deductions = basicSalary * 0.05; // Example 5% deduction for provident fund
        Double tax = basicSalary * 0.10; // Example 10% tax
        Double netSalary = basicSalary + allowances - deductions - tax;

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .paymentDate(LocalDate.now())
                .basicSalary(basicSalary)
                .allowances(allowances)
                .deductions(deductions)
                .tax(tax)
                .netSalary(netSalary)
                .status("PENDING")
                .build();

        return payrollRepository.save(payroll);
    }

    public Payroll markAsPaid(Long payrollId) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));
        payroll.setStatus("PAID");
        return payrollRepository.save(payroll);
    }

    public List<Payroll> getEmployeePayroll(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId);
    }
    
    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }
}
