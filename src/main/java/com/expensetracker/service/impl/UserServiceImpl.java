package com.expensetracker.service.impl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.model.User;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.UserService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Setter(onMethod_ = @__(@Autowired))
public class UserServiceImpl implements UserService {
	
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /*public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }*/

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
