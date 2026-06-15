package com.Comp_Emp_Manage.payroll_service.Entity;

import com.Comp_Emp_Manage.employee_service.Entity.Employee; // Corrected import
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "payroll")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private LocalDate paymentDate;

    @Column(nullable = false)
    private Double basicSalary;
    private Double allowances;
    private Double deductions;
    private Double tax;

    @Column(nullable = false)
    private Double netSalary;

    private String status; // PENDING, PAID

}