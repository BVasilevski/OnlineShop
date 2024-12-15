package org.example.onlineshop.service;

import org.example.onlineshop.model.User;
import org.example.onlineshop.repository.UserRepository;
import org.springframework.stereotype.Service;

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
        return userRepository.findByUsername(username).get();
    }
}
