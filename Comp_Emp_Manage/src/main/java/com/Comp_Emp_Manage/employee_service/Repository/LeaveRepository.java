package com.Comp_Emp_Manage.employee_service.Repository;

import com.Comp_Emp_Manage.employee_service.Entity.Employee;
import com.Comp_Emp_Manage.employee_service.Entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "employee")
    List<Leave> findByEmployee(Employee employee);
    List<Leave> findByStatus(String status);
    
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "employee")
    List<Leave> findAll();
}