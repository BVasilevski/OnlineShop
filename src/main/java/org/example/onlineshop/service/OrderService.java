package org.example.onlineshop.service;

import org.example.onlineshop.model.Order;
import org.example.onlineshop.model.User;
import org.example.onlineshop.repository.OrderRepository;
import org.springframework.data.domain.Sort;
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
        Sort sort = Sort.by("id");
        return this.orderRepository.findAll(sort);
    }

    public void markAsDelivered(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order doesn't exist."));
        order.setDelivered(!order.isDelivered());
        orderRepository.save(order);
    }

    public Order findLastOrder(User user) {
        List<Order> orders = this.orderRepository.findByUserId(user.getId());
        return orders.stream().min((order1, order2) -> Long.compare(order2.getId(), order1.getId())) // Get the first (latest) order
                .orElseThrow(() -> new RuntimeException("No orders found for this user"));
    }

    public void deleteOrder(Long orderId) {
        this.orderRepository.deleteById(orderId);
    }
}
