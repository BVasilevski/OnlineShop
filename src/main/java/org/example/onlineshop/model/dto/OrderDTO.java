package org.example.onlineshop.model.dto;

import java.util.List;

public class OrderDTO {
    public Long id;
    public List<ItemDTO> itemsInOrder;
    public Integer totalPrice;
    public Boolean status;

    public OrderDTO(Long id, List<ItemDTO> itemsInOrder, Integer totalPrice, Boolean status) {
        this.id = id;
        this.itemsInOrder = itemsInOrder;
        this.totalPrice = totalPrice;
        this.status = status;
    }
}
