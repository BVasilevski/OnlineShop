package org.example.onlineshop.service;

import org.example.onlineshop.model.Order;
import org.example.onlineshop.model.User;
import org.example.onlineshop.repository.OrderRepository;
import org.example.onlineshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public boolean exists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User save(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return userRepository.findByUsername(user.getUsername()).get();
        }
        return this.userRepository.save(user);
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    public User registerUser(String email, String name, String lastName, String username, String password, String street, String houseNumber) {
        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User with username already exists.");
        }
        User user = new User(email, name, lastName, username, password, street, houseNumber);
        this.userRepository.save(user);
        return user;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public void saveUserWithDiscount(User user) {
        this.userRepository.save(user);
    }

    public User findByUsernameAndPassword(String username, String password) {
        if (this.userRepository.findByUsernameAndPassword(username, password) != null) {
            return this.userRepository.findByUsernameAndPassword(username, password);
        }
        return null;
    }

    public void incrementUserDiscount(User user) {
        // increment by 2%
        if (user.getDiscount() == 0) {
            user.setDiscount(0.02f);
            return;
        }
        float userDiscount = user.getDiscount();
        float increment = 0.02f;
        float newUserDiscount = userDiscount + increment;
        user.setDiscount(newUserDiscount);

        List<Order> ordersFromUser = orderRepository.getOrdersByUser(user);
        // napravil 5, 10, 15 itn naracka
        if (ordersFromUser.size() % 5 == 0) {
            increment = 0.05f;
            newUserDiscount = user.getDiscount() + increment;
            user.setDiscount(newUserDiscount);
        }

        if (user.getDiscount() > 0.5) {
            user.setDiscount(0.5F);
        }
        userRepository.save(user);
    }

    public void removeUserWithUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException(String.format("User with username %s is not found", username)));
        this.userRepository.delete(user);
    }

    public User findById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException(String.format("User with id: %d doesn't exist", userId)));
    }

    public void updateUser(Long userId, String name, String lastName, String email) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException(String.format("User with id %d doesn't exist", userId)));
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        this.userRepository.save(user);
    }

    public boolean findByEmailAndPassword(String email, String password) {
        return this.userRepository.findByEmailAndPassword(email, password).isPresent();
    }
}
