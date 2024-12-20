package org.example.onlineshop.controller;

import jakarta.servlet.http.HttpSession;
import org.example.onlineshop.model.User;
import org.example.onlineshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String lastName,
                               @RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String street,
                               @RequestParam String houseNumber,
                               Model model) {
        try {
            this.userService.registerUser(name, lastName, username, password, street, houseNumber);
        } catch (RuntimeException e) {
            model.addAttribute("error", "Username already exists.");
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/users")
    public String getUsers(Model model) {
        List<User> users = this.userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/users/set_discount")
    public String setDiscount(@RequestParam String username,
                              @RequestParam Float discount) {
        User user = this.userService.findByUsername(username);
        user.setDiscount(discount);
        userService.saveUserWithDiscount(user);
        return "redirect:/admin/users";
    }
}
