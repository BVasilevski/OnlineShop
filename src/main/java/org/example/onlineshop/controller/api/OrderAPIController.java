package org.example.onlineshop.controller.api;

import org.example.onlineshop.model.User;
import org.example.onlineshop.model.dto.OrderDTO;
import org.example.onlineshop.service.OrderService;
import org.example.onlineshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderAPIController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderAPIController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrdersFromUser(@RequestParam Long userId) {
        try {
            User user = this.userService.findById(userId);
            List<OrderDTO> ordersFromUser = this.orderService.getOrdersFromUserDTO(user);
            return ResponseEntity.ok(ordersFromUser);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUserOrder(@RequestParam Long userId) {
        try {
            this.orderService.createOrder(userId);
            return ResponseEntity.ok("Order created successfully");
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelUserOrder(@PathVariable Long orderId, @RequestParam Long userId) {
        try {
            this.orderService.cancelOrder(userId, orderId);
            return ResponseEntity.ok("Order canceled successfully");
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
