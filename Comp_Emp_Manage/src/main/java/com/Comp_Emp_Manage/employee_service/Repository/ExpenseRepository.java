package com.Comp_Emp_Manage.employee_service.Repository;

import com.Comp_Emp_Manage.employee_service.Entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByEmployeeIdOrderByExpenseDateDesc(Long employeeId);
    List<Expense> findAllByOrderByExpenseDateDesc();
}
