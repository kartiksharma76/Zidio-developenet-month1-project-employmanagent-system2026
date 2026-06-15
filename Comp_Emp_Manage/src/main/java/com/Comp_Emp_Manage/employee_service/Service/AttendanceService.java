package com.Comp_Emp_Manage.employee_service.Service;

import com.Comp_Emp_Manage.employee_service.Entity.Attendance;
import com.Comp_Emp_Manage.employee_service.Entity.Employee;
import com.Comp_Emp_Manage.employee_service.Repository.AttendanceRepository;
import com.Comp_Emp_Manage.employee_service.Repository.EmployeeRepository;
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
                .punchInTime(LocalTime.now())
                .status("PRESENT")
                .build();

        return attendanceRepository.save(attendance);
    }

    public Attendance checkOut(Long employeeId) {
        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() -> new RuntimeException("No check-in record found for today"));

        if (attendance.getPunchOutTime() != null) {
            throw new RuntimeException("Already checked out for today");
        }

        attendance.setPunchOutTime(LocalTime.now());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    public List<Attendance> getAttendanceByEmployeeId(Long employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId);
    }
}
