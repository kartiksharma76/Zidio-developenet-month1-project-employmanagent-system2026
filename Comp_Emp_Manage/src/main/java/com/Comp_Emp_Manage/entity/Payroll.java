package com.Comp_Emp_Manage.entity;

import com.Comp_Emp_Manage.entity.Employee; // Corrected import
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

    private Integer workingDays;
    private Double workingHours;

    private String status; // PENDING, PAID

}