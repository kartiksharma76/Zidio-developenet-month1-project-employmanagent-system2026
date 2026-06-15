package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.entity.Expense;
import com.Comp_Emp_Manage.entity.Employee;
import com.Comp_Emp_Manage.repository.ExpenseRepository;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class WebExpenseController {

    private final ExpenseRepository expenseRepository;
    private final EmployeeRepository employeeRepository;

    @GetMapping
    public String viewExpenses(Model model, org.springframework.security.core.Authentication auth) {
        boolean isAdminOrManager = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (isAdminOrManager) {
            model.addAttribute("expenses", expenseRepository.findAllByOrderByExpenseDateDesc());
        } else {
            Employee emp = employeeRepository.findByEmail(auth.getName()).orElse(null);
            if (emp != null) {
                model.addAttribute("expenses", expenseRepository.findByEmployeeIdOrderByExpenseDateDesc(emp.getId()));
            }
        }
        
        model.addAttribute("isAdminOrManager", isAdminOrManager);
        model.addAttribute("newExpense", new Expense());
        return "expenses";
    }

    @PostMapping("/add")
    public String submitExpense(@ModelAttribute("newExpense") Expense expense, 
                                org.springframework.security.core.Authentication auth,
                                RedirectAttributes redirectAttributes) {
        Employee emp = employeeRepository.findByEmail(auth.getName()).orElse(null);
        if (emp != null) {
            expense.setEmployee(emp);
            expense.setStatus("PENDING");
            expenseRepository.save(expense);
            redirectAttributes.addFlashAttribute("success", "Expense claim submitted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error identifying employee.");
        }
        return "redirect:/expenses";
    }

    @PostMapping("/update-status/{id}")
    public String updateExpenseStatus(@PathVariable Long id, @RequestParam("status") String status, RedirectAttributes redirectAttributes) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense != null) {
            expense.setStatus(status);
            expenseRepository.save(expense);
            redirectAttributes.addFlashAttribute("success", "Expense status updated to " + status + "!");
        }
        return "redirect:/expenses";
    }
}
