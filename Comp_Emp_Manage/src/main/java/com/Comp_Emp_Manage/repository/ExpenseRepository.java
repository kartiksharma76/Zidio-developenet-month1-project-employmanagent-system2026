package com.Comp_Emp_Manage.repository;

import com.Comp_Emp_Manage.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByEmployeeIdOrderByExpenseDateDesc(Long employeeId);
    List<Expense> findAllByOrderByExpenseDateDesc();
}
