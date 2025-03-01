package org.example.onlineshop.requests;


public class UserRequest {
    private Long userId;
    private String email;
    private String name;
    private String lastName;
    private String password;

    public UserRequest() {
    }

    public UserRequest(Long userId, String email, String name, String lastName, String password) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}