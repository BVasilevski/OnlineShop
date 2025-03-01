package org.example.onlineshop.requests;


public class ItemRequest {
    Long userId;
    float rating;
    String comment;

    public ItemRequest(Long userId, float rating, String comment) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
