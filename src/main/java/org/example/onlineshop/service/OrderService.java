package org.example.onlineshop.service;

import org.example.onlineshop.model.Order;
import org.example.onlineshop.model.User;
import org.example.onlineshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrder(Order order) {
        this.orderRepository.save(order);
    }

    public List<Order> getOrdersFromUser(User user) {
        return this.orderRepository.getOrdersByUser(user);
    }

    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    public void markAsDelivered(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order doesn't exist."));
        order.setDelivered(!order.isDelivered());
        orderRepository.save(order);
    }
}
