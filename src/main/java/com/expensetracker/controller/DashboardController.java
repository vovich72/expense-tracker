package com.expensetracker.controller;

import java.time.LocalDate;
import java.util.List;  // ✅ import List

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.expensetracker.model.Expense;
import com.expensetracker.model.User;      // ✅ import User
import com.expensetracker.service.ExpenseService;
import com.expensetracker.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@Setter(onMethod = @__(@Autowired))
public class DashboardController {

    private UserService userService;

    private ExpenseService expenseService;

  /*  @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("loggedInUser");

        if (sessionUser == null) {
            model.addAttribute("error", "Please login first.");
            return "login";
        }

        // ✅ fetch managed User entity from DB
        User dbUser = userService.findByUsername(sessionUser.getUsername());

        // ✅ now this will return actual expenses
        List<Expense> expenses = expenseService.getExpensesByUser(dbUser);

        System.out.println("Found " + expenses.size() + " expenses for user " + dbUser.getUsername());

        model.addAttribute("username", dbUser.getUsername());
        model.addAttribute("expenses", expenses);

        return "dashboard";
    }*/

    @GetMapping("/dashboard")
    public String showDashboard(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/?error";

        List<Expense> expenses = expenseService.getExpensesByFilter(user, category, fromDate, toDate);

        model.addAttribute("expenses", expenses);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "dashboard";
    }

}

