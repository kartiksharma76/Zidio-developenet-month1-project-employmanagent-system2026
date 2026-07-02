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
        long startSec = punchInTime.toSecondOfDay();
        long endSec = punchOutTime.toSecondOfDay();
        long diffSec;
        if (endSec >= startSec) {
            diffSec = endSec - startSec;
        } else {
            // Overnight shift (e.g. 10 PM to 6 AM)
            diffSec = (24 * 3600 - startSec) + endSec;
        }
        return diffSec / 3600.0;
    }

    public String getWorkingHoursFormatted() {
        if (punchInTime == null || punchOutTime == null) {
            return "-";
        }
        long startSec = punchInTime.toSecondOfDay();
        long endSec = punchOutTime.toSecondOfDay();
        long diffSec;
        if (endSec >= startSec) {
            diffSec = endSec - startSec;
        } else {
            // Overnight shift (e.g. 10 PM to 6 AM)
            diffSec = (24 * 3600 - startSec) + endSec;
        }
        long hours = diffSec / 3600;
        long minutes = (diffSec % 3600) / 60;
        return String.format("%dh %dm", hours, minutes);
    }

}