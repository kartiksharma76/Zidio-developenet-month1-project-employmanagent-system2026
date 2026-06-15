package com.Comp_Emp_Manage.repository;

import com.Comp_Emp_Manage.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "employee")
    List<Payroll> findByEmployeeId(Long employeeId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "employee")
    List<Payroll> findAll();
}