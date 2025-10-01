package com.expensetracker.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.service.ExpenseService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/expenses")
@Setter(onMethod = @__(@Autowired))
public class ExpenseController {

    private ExpenseService expenseService;

    @PostMapping("/addExpense")
    public String addExpense(@RequestParam BigDecimal amount,
                             @RequestParam String category,
                             @RequestParam String date,
                             @RequestParam(required = false) String description,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to add expenses.");
            return "redirect:/login";
        }

        try {
            Expense expense = new Expense();
            expense.setAmount(amount);
            expense.setCategory(category);
            expense.setDescription(description);
            expense.setDate(LocalDate.parse(date));
            expense.setUser(user);

            expenseService.saveExpense(expense);

            redirectAttributes.addFlashAttribute("success", "Expense added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add expense: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    // ✅ Delete expense
    @PostMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/?error";

        expenseService.deleteExpense(id, user);
        return "redirect:/dashboard?deleted";
    }

   /* // ✅ Show edit form
    @GetMapping("/edit/{id}")
    public String editExpenseForm(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/?error";

        Expense expense = expenseService.getExpenseById(id, user);
        if (expense == null) return "redirect:/dashboard?notfound";

        model.addAttribute("expense", expense);
        return "edit-expense"; // ➝ create edit-expense.html
    }*/
 // Show edit form

    @GetMapping("/edit/{id}")
    public String editExpenseForm(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/?error";

        Expense expense = expenseService.getExpenseById(id, user);
        if (expense == null) return "redirect:/dashboard?notfound";

        model.addAttribute("expense", expense);
        return "edit-expense"; // Thymeleaf template
    }


    // ✅ Handle edit submit
    @PostMapping("/update")
    public String updateExpense(@ModelAttribute Expense expense, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/?error";

        expense.setUser(user); // re-attach logged-in user
        expenseService.saveExpense(expense);
        return "redirect:/dashboard?updated";
    }
    
    @GetMapping("/download")
    public void downloadExpensesCsv(HttpSession session, HttpServletResponse response) throws IOException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            response.sendRedirect("/?error");
            return;
        }

        List<Expense> expenses = expenseService.getExpensesByUser(user);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"expenses.csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("Date,Amount,Category,Description");

        for (Expense expense : expenses) {
            writer.println(String.format("%s,%.2f,%s,%s",
                    expense.getDate() != null ? expense.getDate() : "",
                    expense.getAmount() != null ? expense.getAmount().floatValue() : 0,
                    expense.getCategory() != null ? expense.getCategory() : "",
                    expense.getDescription() != null ? expense.getDescription().replaceAll(",", ";") : ""));
        }
        writer.flush();
        writer.close();
    }

}
