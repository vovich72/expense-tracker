package com.expensetracker.service;

import java.time.LocalDate;
import java.util.List;

import com.expensetracker.model.Expense;
import com.expensetracker.model.User;

public interface ExpenseService {

    Expense saveExpense(Expense expense);
    List<Expense> getExpensesByUser(User user);
    
    // ✅ Add two methods
    Expense getExpenseById(Long id, User user);
    void deleteExpense(Long id, User user);
    List<Expense> getExpensesByFilter(User user, String category, LocalDate fromDate, LocalDate toDate);

}
