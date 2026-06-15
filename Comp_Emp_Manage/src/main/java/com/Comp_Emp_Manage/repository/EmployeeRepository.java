package com.Comp_Emp_Manage.repository;

import com.Comp_Emp_Manage.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);
    java.util.Optional<Employee> findByEmail(String email);
}
