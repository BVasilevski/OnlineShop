package org.example.onlineshop.model.dto;

import java.util.List;

public class ItemDTO {
    private Long id;
    private String name;
    private float price;
    private String imageUrl;
    private List<ItemRatingDTO> itemRatings;

    public ItemDTO(Long id, String name, float price, String imageUrl, List<ItemRatingDTO> itemRatings) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.itemRatings = itemRatings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ItemRatingDTO> getItemRatings() {
        return itemRatings;
    }

    public void setItemRatings(List<ItemRatingDTO> itemRatings) {
        this.itemRatings = itemRatings;
    }
}
