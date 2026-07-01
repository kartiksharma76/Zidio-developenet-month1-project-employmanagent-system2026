package com.Comp_Emp_Manage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "performance_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private LocalDate reviewDate;

    @Column(nullable = false)
    private Integer rating; // e.g. 1 to 5

    @Column(length = 1000)
    private String goals;

    @Column(length = 2000)
    private String feedback;

    private String reviewerName;

    private String reviewType; // MONTHLY, QUARTERLY, YEARLY

    private Integer presentCount;

    private Integer absentCount;
}