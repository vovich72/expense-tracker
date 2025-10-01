package com.expensetracker.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.expensetracker.model.Expense;
import com.opencsv.CSVWriter;

@Component
public class CSVExporter {
    public void exportExpenses(List<Expense> expenses, String filePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            writer.writeNext(new String[]{"Date", "Category", "Amount", "Description"});

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Expense expense : expenses) {
                String categoryName = (expense.getCategory() != null) 
                                        ? expense.getCategory()   // <-- use category field
                                        : "Uncategorized";

                String[] line = {
                    expense.getDate() != null ? expense.getDate().format(formatter) : "",
                    categoryName,
                    expense.getAmount() != null ? expense.getAmount().toString() : "0.00",
                    expense.getDescription() != null ? expense.getDescription() : ""
                };

                writer.writeNext(line);
            }
        }
    }
}
