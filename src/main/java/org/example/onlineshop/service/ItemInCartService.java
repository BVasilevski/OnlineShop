package org.example.onlineshop.service;

import jakarta.transaction.Transactional;
import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.ItemInCart;
import org.example.onlineshop.model.User;
import org.example.onlineshop.repository.ItemInCartRepository;
import org.example.onlineshop.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemInCartService {
    private final ItemInCartRepository itemInCartRepository;
    private final ItemRepository itemRepository;

    public ItemInCartService(ItemInCartRepository itemInCartRepository, ItemRepository itemRepository) {
        this.itemInCartRepository = itemInCartRepository;
        this.itemRepository = itemRepository;
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

    public void removeFromUserCart(User user, Long itemId) {
        this.itemInCartRepository.deleteById(itemId);
    }
}
