package com.expensetracker.service;

import com.expensetracker.model.Category;
import com.expensetracker.model.User;

import java.util.List;

public interface CategoryService {

    Category findByNameAndUser(String name, User user);
    Category saveCategory(Category category);
    List<Category> findAllByUser(User user);
}
