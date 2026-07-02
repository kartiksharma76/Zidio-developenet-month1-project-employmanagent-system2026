package com.Comp_Emp_Manage.service;

import com.Comp_Emp_Manage.entity.Payroll;
import com.Comp_Emp_Manage.repository.PayrollRepository;
import com.Comp_Emp_Manage.entity.Employee;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;

    public Payroll generatePayroll(Long employeeId, Double allowances) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Double baseSalary = employee.getSalary() != null ? employee.getSalary() : 0.0;

        // Calculate attendance stats for the current month
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        List<com.Comp_Emp_Manage.entity.Attendance> attendanceList = attendanceRepository.findByEmployeeId(employeeId);

        int workingDays = (int) attendanceList.stream()
                .filter(a -> !a.getDate().isBefore(startOfMonth) && !a.getDate().isAfter(endOfMonth))
                .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                .count();

        double workingHours = attendanceList.stream()
                .filter(a -> !a.getDate().isBefore(startOfMonth) && !a.getDate().isAfter(endOfMonth))
                .mapToDouble(a -> a.getWorkingHours() != null ? a.getWorkingHours() : 0.0)
                .sum();

        // Calculate dynamic basic salary based on working hours.
        // Assuming standard 22 working days per month and 8 working hours per day (176 total hours).
        double standardHours = 176.0;
        double calculatedBasicSalary = 0.0;
        if (baseSalary > 0 && workingHours > 0) {
            calculatedBasicSalary = baseSalary * (workingHours / standardHours);
            calculatedBasicSalary = Math.min(baseSalary, calculatedBasicSalary); // Cap at base contract salary
            // Round to 2 decimal places
            calculatedBasicSalary = Math.round(calculatedBasicSalary * 100.0) / 100.0;
        }

        Double deductions = calculatedBasicSalary * 0.05; // 5% PF
        deductions = Math.round(deductions * 100.0) / 100.0;

        Double tax = calculatedBasicSalary * 0.10; // 10% tax
        tax = Math.round(tax * 100.0) / 100.0;

        Double netSalary = calculatedBasicSalary + allowances - deductions - tax;
        netSalary = Math.round(netSalary * 100.0) / 100.0;

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .paymentDate(LocalDate.now())
                .basicSalary(calculatedBasicSalary)
                .allowances(allowances)
                .deductions(deductions)
                .tax(tax)
                .netSalary(netSalary)
                .workingDays(workingDays)
                .workingHours(workingHours)
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
