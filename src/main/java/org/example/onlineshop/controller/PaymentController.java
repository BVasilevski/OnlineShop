package org.example.onlineshop.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpSession;
import org.example.onlineshop.model.Order;
import org.example.onlineshop.model.User;
import org.example.onlineshop.service.ItemInCartService;
import org.example.onlineshop.service.OrderService;
import org.example.onlineshop.service.StripeService;
import org.example.onlineshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final StripeService stripeService;
    private final OrderService orderService;
    private final ItemInCartService itemInCartService;
    private final UserService userService;

    public PaymentController(StripeService stripeService, OrderService orderService, ItemInCartService itemInCartService, UserService userService) {
        this.stripeService = stripeService;
        this.orderService = orderService;
        this.itemInCartService = itemInCartService;
        this.userService = userService;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> paymentData, HttpSession session) {
        try {
            Order order = (Order) session.getAttribute("order");
            Long amount = (long) order.getTotalPrice();
            String currency = paymentData.get("currency").toString();
            String description = paymentData.get("description").toString();

            PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, currency, description);
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());

            orderService.saveOrder(order);
            session.removeAttribute("order");

            User user = (User) session.getAttribute("user");
            itemInCartService.removeItemsFromCart(user);
            userService.incrementUserDiscount(user);
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            e.printStackTrace();
            session.removeAttribute("order");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Stripe error: " + e.getMessage());
        } catch (Exception e) {
            session.removeAttribute("order");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/clear_cart")
    public ResponseEntity<?> clearUserCart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        orderService.saveOrder((Order) session.getAttribute("order"));
        session.removeAttribute("order");
        itemInCartService.removeItemsFromCart(user);
        return ResponseEntity.ok("Payment created successfully.");
    }
}

