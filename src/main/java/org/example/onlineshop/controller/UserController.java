package org.example.onlineshop.controller;

import jakarta.servlet.http.HttpSession;
import org.example.onlineshop.model.ItemInCart;
import org.example.onlineshop.model.ShopRating;
import org.example.onlineshop.model.User;
import org.example.onlineshop.service.ItemInCartService;
import org.example.onlineshop.service.ShopRatingService;
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
    private final ItemInCartService itemInCartService;
    private final ShopRatingService shopRatingService;

    public UserController(UserService userService, ItemInCartService itemInCartService, ShopRatingService shopRatingService) {
        this.userService = userService;
        this.itemInCartService = itemInCartService;
        this.shopRatingService = shopRatingService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        User user = null;
        if (userService.findByUsernameAndPassword(username, password) != null) {
            user = userService.findByUsernameAndPassword(username, password);
            session.setAttribute("user", user);
            if (session.getAttribute("itemInCarts") != null) {
                List<ItemInCart> itemInCarts = (List<ItemInCart>) session.getAttribute("itemInCarts");
                itemInCartService.addItemsToUserCart(user, itemInCarts);
                session.removeAttribute("itemInCarts");
                return "redirect:/items/cart";
            }
            return "redirect:/items";
        } else {
            model.addAttribute("error", "Incorrect username or password.");
            return "login";
        }
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
    public String registerUser(@RequestParam String email,
                               @RequestParam String name,
                               @RequestParam String lastName,
                               @RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String street,
                               @RequestParam String houseNumber,
                               HttpSession session,
                               Model model) {
        try {
            User user = this.userService.registerUser(email, name, lastName, username, password, street, houseNumber);
            session.setAttribute("user", user);
            if (session.getAttribute("itemInCarts") != null) {
                List<ItemInCart> itemInCarts = (List<ItemInCart>) session.getAttribute("itemInCarts");
                this.itemInCartService.addItemsToUserCart(user, itemInCarts);
                session.removeAttribute("itemInCarts");
                return "redirect:/items/cart";
            }
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

    @GetMapping("/rating")
    public String getRatingPage() {
        return "rating_page";
    }

    @GetMapping("/ratings")
    public String showStoreRatings(Model model) {
        List<ShopRating> ratings = this.shopRatingService.findAll();
        model.addAttribute("ratings", ratings);
        return "ratings";
    }

    @PostMapping("/submitRating")
    public String addRatingForStore(@RequestParam Integer rating,
                                    @RequestParam String comment,
                                    HttpSession session) {
        User user = (User) session.getAttribute("user");
        ShopRating shopRating = new ShopRating(comment, rating, user);
        this.shopRatingService.save(shopRating);
        return "redirect:/ratings";
    }

    @PostMapping("/users/remove")
    public String removeUser(@RequestParam String username) {
        this.userService.removeUserWithUsername(username);
        return "redirect:/admin/users";
    }
}
