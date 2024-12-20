package org.example.onlineshop.service;

import org.example.onlineshop.model.User;
import org.example.onlineshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public void registerUser(String name, String lastName, String username, String password, String street, String houseNumber) {
        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User with username already exists.");
        }
        User user = new User(name, lastName, username, password, street, houseNumber);
        this.userRepository.save(user);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public void saveUserWithDiscount(User user) {
        this.userRepository.save(user);
    }
}
