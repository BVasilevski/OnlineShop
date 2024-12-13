package org.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.onlineshop.model.enumerations.Category;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@Data
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private float price;

    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    private float avgRating;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemInCart> cartItems;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemRating> ratings;

    @ManyToMany(mappedBy = "items")
    private List<Order> orders;

    public Item(String imageUrl, float price, String name, Category category) {
        this.imageUrl = imageUrl;
        this.price = price;
        this.name = name;
        this.category = category;
        this.cartItems = new HashSet<>();
        this.ratings = new HashSet<>();
    }

    public Item() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public Set<ItemInCart> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<ItemInCart> cartItems) {
        this.cartItems = cartItems;
    }

    public Set<ItemRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<ItemRating> ratings) {
        this.ratings = ratings;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}