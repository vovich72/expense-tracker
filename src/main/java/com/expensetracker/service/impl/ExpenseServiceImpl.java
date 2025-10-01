package com.expensetracker.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.service.ExpenseService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@Setter(onMethod_ = @__(@Autowired))
public class ExpenseServiceImpl implements ExpenseService {

    private ExpenseRepository expenseRepository;

    @Override
    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    @Override
    public Expense getExpenseById(Long id, User user) {
        return expenseRepository.findById(id)
                .filter(exp -> exp.getUser().getId().equals(user.getId()))
                .orElse(null);
    }

    @Transactional
    @Override
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Transactional
    @Override
    public void deleteExpense(Long id, User user) {
        expenseRepository.findById(id).ifPresent(exp -> {
            if (exp.getUser().getId().equals(user.getId())) {
                expenseRepository.delete(exp);
            }
        });
    }

    @Override
    public List<Expense> getExpensesByFilter(User user, String category, LocalDate fromDate, LocalDate toDate) {
        List<Expense> all = expenseRepository.findByUser(user);

        return all.stream()
                .filter(e -> category == null || category.isEmpty() || e.getCategory().equalsIgnoreCase(category))
                .filter(e -> fromDate == null || (e.getDate() != null && !e.getDate().isBefore(fromDate)))
                .filter(e -> toDate == null || (e.getDate() != null && !e.getDate().isAfter(toDate)))
                .collect(Collectors.toList());
    }

}
