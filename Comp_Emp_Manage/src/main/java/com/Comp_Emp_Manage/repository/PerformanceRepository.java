package com.Comp_Emp_Manage.repository;

import com.Comp_Emp_Manage.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "employee")
    List<Performance> findByEmployeeId(Long employeeId);
    
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "employee")
    List<Performance> findAll();
}