package com.Comp_Emp_Manage.employee_service.Repository;

import com.Comp_Emp_Manage.employee_service.Entity.Attendance;
import com.Comp_Emp_Manage.employee_service.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);
    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
    
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "employee")
    java.util.List<Attendance> findByEmployeeId(Long employeeId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "employee")
    java.util.List<Attendance> findAll();
}