package org.example.onlineshop.controller.api;

import org.example.onlineshop.model.User;
import org.example.onlineshop.model.dto.UserDTO;
import org.example.onlineshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserAPIController {
    private final UserService userService;

    public UserAPIController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = this.userService.findAllDTO();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewUser(@RequestParam String email,
                                           @RequestParam String name,
                                           @RequestParam String lastName,
                                           @RequestParam String password) {
        User user = new User(email, name, lastName, password);
        this.userService.save(user);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/edit/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
                                        @RequestParam String name,
                                        @RequestParam String lastName,
                                        @RequestParam String email) {
        try {
            this.userService.updateUser(userId, name, lastName, email);
            return ResponseEntity.ok("User updated successfully");
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email,
                                       @RequestParam String password) {
        Optional<User> user = this.userService.findByEmailAndPassword(email, password);
        if (user.isPresent()) {
            return ResponseEntity.ok(Map.of("userId", user.get().getId()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in the database");
        }
    }
}
