package org.example.onlineshop.controller;

import org.example.onlineshop.model.Order;
import org.example.onlineshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DelivererController {
    private final OrderService orderService;

    public DelivererController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all-orders")
    public String getAllOrders(Model model) {
        List<Order> orderList = this.orderService.findAll();
        model.addAttribute("orders", orderList);
        return "orders";
    }
}
