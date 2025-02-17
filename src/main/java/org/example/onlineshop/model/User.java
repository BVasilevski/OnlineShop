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

    private String email;

    private String name;

    private String lastName;

    private String username;

    private String password;

    private String streetNumber;

    private String houseNumber;

    private float discount;

    @Enumerated(EnumType.STRING)
    private UserType type;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ItemInCart> cartItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemRating> ratings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShopRating> shopRatings;

    public User(String email, String name, String lastName, String username, String password, String streetNumber, String houseNumber) {
        this.email = email;
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

    public User(String email, String name, String lastName, String password) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
    }

    public Long getId() {
        return id;
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

    public Set<ShopRating> getShopRatings() {
        return shopRatings;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public void setCartItems(Set<ItemInCart> cartItems) {
        this.cartItems = cartItems;
    }

    public void setRatings(Set<ItemRating> ratings) {
        this.ratings = ratings;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void setShopRatings(Set<ShopRating> shopRatings) {
        this.shopRatings = shopRatings;
    }
}
