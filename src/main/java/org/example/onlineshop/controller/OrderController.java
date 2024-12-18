package org.example.onlineshop.controller;

import jakarta.servlet.http.HttpSession;
import org.example.onlineshop.model.Order;
import org.example.onlineshop.model.User;
import org.example.onlineshop.model.enumerations.UserType;
import org.example.onlineshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrdersForUser(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Order> orders;
        if (user.getType().equals(UserType.ADMIN) || user.getType().equals(UserType.DELIVERER)) {
            orders = this.orderService.findAll();
        } else {
            orders = this.orderService.getOrdersFromUser(user);
        }
        model.addAttribute("orders", orders);
        return "orders";
    }
}
