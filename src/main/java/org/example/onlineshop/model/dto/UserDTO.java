package org.example.onlineshop.model.dto;

public class UserDTO {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public UserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
