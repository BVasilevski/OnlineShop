package org.example.onlineshop.service;

import org.example.onlineshop.model.Item;
import org.example.onlineshop.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository productRepository) {
        this.itemRepository = productRepository;
    }

    // Save or update product
    public void save(Item item) {
        itemRepository.save(item);
    }

    // Find all products
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    // Find product by ID
    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Delete product by ID
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}
