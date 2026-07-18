package com.Comp_Emp_Manage.service;

import com.Comp_Emp_Manage.entity.Attendance;
import com.Comp_Emp_Manage.entity.Employee;
import com.Comp_Emp_Manage.repository.AttendanceRepository;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public Attendance checkIn(Long employeeId) {
        autoPunchOutPreviousDaysForEmployee(employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate today = LocalDate.now();

        // Check if already checked in
        if (attendanceRepository.findByEmployeeIdAndDate(employeeId, today).isPresent()) {
            throw new RuntimeException("Already checked in for today");
        }

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .date(today)
                .punchInTime(LocalTime.now().withNano(0))
                .status("PRESENT")
                .build();

        return attendanceRepository.save(attendance);
    }

    public Attendance checkOut(Long employeeId) {
        autoPunchOutPreviousDaysForEmployee(employeeId);

        Attendance attendance = attendanceRepository.findFirstByEmployeeIdAndPunchOutTimeIsNullOrderByDateDesc(employeeId)
                .orElseThrow(() -> new RuntimeException("No active check-in record found. Please punch in first."));

        java.time.LocalDateTime nowDateTime = java.time.LocalDateTime.now();
        LocalTime punchIn = attendance.getPunchInTime();
        if (punchIn != null) {
            java.time.LocalDateTime punchInDateTime = java.time.LocalDateTime.of(attendance.getDate(), punchIn);
            java.time.Duration duration = java.time.Duration.between(punchInDateTime, nowDateTime);
            if (duration.toMinutes() < 300) { // 5 hours = 300 minutes
                long remainingMinutes = 300 - duration.toMinutes();
                long remainingHours = remainingMinutes / 60;
                long mins = remainingMinutes % 60;
                throw new RuntimeException(String.format(
                    "You can only punch out after 5 hours of punching in. Remaining time: %d hours and %d minutes.",
                    remainingHours, mins
                ));
            }
        }

        attendance.setPunchOutTime(nowDateTime.toLocalTime().withNano(0));
        attendance.setPunchOutDate(nowDateTime.toLocalDate());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAllAttendance() {
        autoPunchOutPreviousDays();
        return attendanceRepository.findAll();
    }

    public List<Attendance> getAttendanceByEmployeeId(Long employeeId) {
        autoPunchOutPreviousDaysForEmployee(employeeId);
        return attendanceRepository.findByEmployeeId(employeeId);
    }

    public void autoPunchOutPreviousDays() {
        LocalDate today = LocalDate.now();
        List<Attendance> openRecords = attendanceRepository.findByPunchOutTimeIsNullAndDateLessThan(today);
        for (Attendance record : openRecords) {
            autoPunchOutRecord(record);
        }
    }

    public void autoPunchOutPreviousDaysForEmployee(Long employeeId) {
        LocalDate today = LocalDate.now();
        List<Attendance> openRecords = attendanceRepository.findByEmployeeIdAndPunchOutTimeIsNullAndDateLessThan(employeeId, today);
        for (Attendance record : openRecords) {
            autoPunchOutRecord(record);
        }
    }

    private void autoPunchOutRecord(Attendance record) {
        LocalTime punchIn = record.getPunchInTime();
        if (punchIn != null) {
            LocalTime punchOut = punchIn.plusHours(9);
            LocalDate punchOutDate = record.getDate();
            if (punchOut.isBefore(punchIn)) {
                punchOutDate = punchOutDate.plusDays(1);
            }
            record.setPunchOutTime(punchOut);
            record.setPunchOutDate(punchOutDate);
        } else {
            record.setPunchOutTime(LocalTime.of(18, 0, 0));
            record.setPunchOutDate(record.getDate());
        }
        attendanceRepository.save(record);
    }
}
