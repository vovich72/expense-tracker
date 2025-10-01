package com.expensetracker.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.expensetracker.model.User;
import com.expensetracker.service.UserService;


@Controller
@Setter(onMethod = @__(@Autowired))
public class Registration {

    private UserService userService;

    private PasswordEncoder passwordEncoder;

	@GetMapping("/register")
	public String showRegisterForm() {
	    return "register";
	}

	@PostMapping("/register")
	public String registerUser(@RequestParam String username,
	                           @RequestParam String email,
	                           @RequestParam String password,
	                           @RequestParam String confirmPassword,
	                           Model model) {

	    // Validate passwords
	    if (!password.equals(confirmPassword)) {
	        model.addAttribute("error", "Passwords do not match!");
	        return "register";
	    }

	    // Check if username exists
	    if (userService.findByUsername(username) != null) {
	        model.addAttribute("error", "Username already taken!");
	        return "register";
	    }

	    // Create new user
	    User user = new User();
	    user.setUsername(username);
	    user.setEmail(email);

	    // ✅ Encrypt password before saving
	    user.setPassword(passwordEncoder.encode(password));

	    userService.save(user);

	    // Redirect to login page with success message
	    return "redirect:/?success";
	}


}
