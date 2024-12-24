package org.example.onlineshop.model;

import jakarta.persistence.*;

@Entity
public class ShopRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private int rating;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ShopRating(String comment, int rating, User user) {
        this.comment = comment;
        this.rating = rating;
        this.user = user;
    }

    public ShopRating() {
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }
}
