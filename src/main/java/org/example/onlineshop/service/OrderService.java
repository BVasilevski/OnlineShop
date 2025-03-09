package org.example.onlineshop.service;

import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.ItemInCart;
import org.example.onlineshop.model.Order;
import org.example.onlineshop.model.User;
import org.example.onlineshop.model.dto.ItemDTO;
import org.example.onlineshop.model.dto.OrderDTO;
import org.example.onlineshop.repository.OrderRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ItemInCartService itemInCartService;

    public OrderService(OrderRepository orderRepository, UserService userService, ItemInCartService itemInCartService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.itemInCartService = itemInCartService;
    }

    public void saveOrder(Order order) {
        this.orderRepository.save(order);
    }

    public List<Order> getOrdersFromUser(User user) {
        return this.orderRepository.getOrdersByUser(user);
    }

    public List<OrderDTO> getOrdersFromUserDTO(User user) {
        List<Order> allOrders = this.orderRepository.getOrdersByUser(user);
        return allOrders.stream().map(order -> new OrderDTO(order.getId(),
                order.getItems().stream().map(item -> new ItemDTO(item.getId(), item.getName(), item.getPrice(), item.getImageUrl(), null)).collect(Collectors.toList()),
                (int) order.getTotalPrice(), order.isDelivered())).toList();
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

    public void createOrder(Long userId) {
        User user = this.userService.findById(userId);
        List<ItemInCart> itemInCarts = this.itemInCartService.getAllItemsInUserCart(user);
        double totalPrice = itemInCarts.stream().mapToDouble(item -> item.getQuantity() * item.getItem().getPrice()).sum();
        List<Item> items = itemInCarts.stream().map(ItemInCart::getItem).toList();
        Order order = new Order((float) totalPrice, user, items);
        this.itemInCartService.removeItemsFromCart(user);
        this.orderRepository.save(order);
    }
}
