package org.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class ItemRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private float rating;

    private String comment;

    private String userImageUrl;

    public ItemRating(Item item, User user, float rating, String comment, String userImageUrl) {
        this.item = item;
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.userImageUrl = userImageUrl;
    }

    public ItemRating() {
    }

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public User getUser() {
        return user;
    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }
}
