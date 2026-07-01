package com.Comp_Emp_Manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate date;

    private LocalTime punchInTime;
    
    private LocalTime punchOutTime;

    private String status; // PRESENT, ABSENT, HALF_DAY

    public Double getWorkingHours() {
        if (punchInTime == null || punchOutTime == null) {
            return 0.0;
        }
        long seconds = java.time.Duration.between(punchInTime, punchOutTime).getSeconds();
        return seconds / 3600.0;
    }

    public String getWorkingHoursFormatted() {
        if (punchInTime == null || punchOutTime == null) {
            return "-";
        }
        java.time.Duration duration = java.time.Duration.between(punchInTime, punchOutTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%dh %dm", hours, minutes);
    }

}