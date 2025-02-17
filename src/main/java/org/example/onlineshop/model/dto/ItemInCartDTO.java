package org.example.onlineshop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemInCartDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("itemName")
    private String itemName;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("price")
    private float price;
    @JsonProperty("quantity")
    private int quantity;

    public ItemInCartDTO(Long id, String itemName, String imageUrl, float price, int quantity) {
        this.id = id;
        this.itemName = itemName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getItemName() {
        return this.itemName;
    }

    public int getQuantity() {
        return quantity;
    }
}
