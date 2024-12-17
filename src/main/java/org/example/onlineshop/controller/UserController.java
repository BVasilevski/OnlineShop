package org.example.onlineshop.controller;

import jakarta.servlet.http.HttpSession;
import org.example.onlineshop.model.User;
import org.example.onlineshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session) {
        User user = null;
        if (userService.exists(username)) {
            user = userService.findByUsername(username);
        } else {
            user = new User("Bojan", "Vasilevski", username, password, "7", "38A");
            this.userService.save(user);
        }
        session.setAttribute("user", user);
        return "redirect:/items";
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/items";
    }
}
