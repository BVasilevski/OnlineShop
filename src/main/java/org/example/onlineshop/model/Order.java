package org.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float totalPrice;

    private boolean delivered;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "order_product", // Join table name
            joinColumns = @JoinColumn(name = "order_id"), // Foreign key to Order
            inverseJoinColumns = @JoinColumn(name = "product_id") // Foreign key to Product
    )
    private List<Item> items = new ArrayList<>();

    public Order(float totalPrice) {
        this.totalPrice = totalPrice;
        this.delivered = false;
    }
}
