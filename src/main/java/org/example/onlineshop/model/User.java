package org.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.example.onlineshop.model.enumerations.UserType;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastName;

    private String username;

    private String password;

    private String streetNumber;

    private String houseNumber;

    private float discount;

    @Enumerated(EnumType.STRING)
    private UserType type;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<ItemInCart> cartItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemRating> ratings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShopRating> shopRatings;

    public User(String name, String lastName, String username, String password, String streetNumber, String houseNumber) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.streetNumber = streetNumber;
        this.houseNumber = houseNumber;
        this.discount = 0.0F;
        this.type = UserType.USER;
        this.cartItems = new HashSet<>();
        this.ratings = new HashSet<>();
        this.orders = new HashSet<>();
        this.shopRatings = new HashSet<>();
    }

    public User(UserType type) {
        this.type = type;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public float getDiscount() {
        return discount;
    }

    public UserType getType() {
        return type;
    }

    public Set<ItemInCart> getCartItems() {
        return cartItems;
    }

    public Set<ItemRating> getRatings() {
        return ratings;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}
