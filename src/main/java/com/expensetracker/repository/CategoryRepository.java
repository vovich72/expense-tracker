package com.expensetracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensetracker.model.Category;
import com.expensetracker.model.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameAndUser(String name, User user);

    List<Category> findAllByUser(User user);
}