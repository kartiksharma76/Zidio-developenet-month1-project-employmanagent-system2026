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

    private LocalDate punchOutDate;

    private String status; // PRESENT, ABSENT, HALF_DAY

    public String getPunchInTimeFormatted() {
        if (punchInTime == null) {
            return "-";
        }
        return punchInTime.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public String getPunchOutTimeFormatted() {
        if (punchOutTime == null) {
            return "-";
        }
        return punchOutTime.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public Double getWorkingHours() {
        if (punchInTime == null || punchOutTime == null) {
            return 0.0;
        }
        LocalDate outDate = punchOutDate != null ? punchOutDate : date;
        java.time.LocalDateTime start = java.time.LocalDateTime.of(date, punchInTime);
        java.time.LocalDateTime end = java.time.LocalDateTime.of(outDate, punchOutTime);
        if (end.isBefore(start)) {
            return 0.0;
        }
        long seconds = java.time.Duration.between(start, end).getSeconds();
        return seconds / 3600.0;
    }

    public String getWorkingHoursFormatted() {
        if (punchInTime == null || punchOutTime == null) {
            return "-";
        }
        LocalDate outDate = punchOutDate != null ? punchOutDate : date;
        java.time.LocalDateTime start = java.time.LocalDateTime.of(date, punchInTime);
        java.time.LocalDateTime end = java.time.LocalDateTime.of(outDate, punchOutTime);
        if (end.isBefore(start)) {
            return "0h 0m";
        }
        java.time.Duration duration = java.time.Duration.between(start, end);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%dh %dm", hours, minutes);
    }

}