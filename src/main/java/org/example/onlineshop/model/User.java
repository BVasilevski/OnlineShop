package org.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastName;

    private String streetNumber;

    private String houseNumber;

    private float discount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemInCart> cartItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemRating> ratings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;

    public User(String name, String lastName, String streetNumber, String houseNumber) {
        this.name = name;
        this.lastName = lastName;
        this.streetNumber = streetNumber;
        this.houseNumber = houseNumber;
        this.discount = 0.0F;
        this.cartItems = new HashSet<>();
        this.ratings = new HashSet<>();
        this.orders = new HashSet<>();
    }
}
