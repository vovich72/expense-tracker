package com.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.expensetracker.model.User;
import com.expensetracker.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/doLogin")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        System.out.println("username = " + username + ", password = " + password);

        User user = userService.findByUsername(username); // make sure this returns null if user not found

        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        System.out.println("user = " + user + ", passwordMatches = " + passwordMatches);

        if (passwordMatches) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        // ✅ Invalidate the current session
        session.invalidate();

        // ✅ Redirect back to login page with a success message
        return "redirect:/?logout";
    }

}
