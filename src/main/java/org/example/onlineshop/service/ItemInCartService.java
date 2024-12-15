package org.example.onlineshop.service;

import jakarta.transaction.Transactional;
import org.example.onlineshop.model.ItemInCart;
import org.example.onlineshop.model.User;
import org.example.onlineshop.repository.ItemInCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemInCartService {
    private final ItemInCartRepository itemInCartRepository;

    public ItemInCartService(ItemInCartRepository itemInCartRepository) {
        this.itemInCartRepository = itemInCartRepository;
    }

    public List<ItemInCart> getAllItemsInUserCart(User user) {
        return itemInCartRepository.findAllByUser(user);
    }

    public List<ItemInCart> getAll() {
        return itemInCartRepository.findAll();
    }

    @Transactional
    public void save(ItemInCart itemInCart) {
        itemInCartRepository.save(itemInCart);
    }
}
