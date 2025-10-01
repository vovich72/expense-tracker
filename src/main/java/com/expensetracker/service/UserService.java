package com.expensetracker.service;

import com.expensetracker.model.User;

public interface UserService {

    User findByUsername(String username);
    User save(User user);
}
