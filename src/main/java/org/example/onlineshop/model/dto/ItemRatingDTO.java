package org.example.onlineshop.model.dto;

public class ItemRatingDTO {
    private Long id;
    private String userEmail;
    private float rating;
    private String comment;
    private String userImageUrl;

    public ItemRatingDTO(Long id, String userEmail, float rating, String comment, String userImageUrl) {
        this.id = id;
        this.userEmail = userEmail;
        this.rating = rating;
        this.comment = comment;
        this.userImageUrl = userImageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
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
