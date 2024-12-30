package org.example.onlineshop.service;

import jakarta.transaction.Transactional;
import org.example.onlineshop.model.ItemInCart;
import org.example.onlineshop.model.User;
import org.example.onlineshop.repository.ItemInCartRepository;
import org.springframework.data.domain.Sort;
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
        Sort sort = Sort.by("quantity").descending();
        return itemInCartRepository.findAll(sort);
    }

    @Transactional
    public void save(ItemInCart itemInCart) {
        itemInCartRepository.save(itemInCart);
    }

    public void removeFromUserCart(User user, Long itemId) {
        this.itemInCartRepository.deleteById(itemId);
    }

    public void removeItemsFromCart(User user) {
        List<ItemInCart> items = itemInCartRepository.findAllByUser(user);
        itemInCartRepository.deleteAll(items);
    }

    @Transactional
    public void addItemsToUserCart(User user, List<ItemInCart> itemInCarts) {
        List<ItemInCart> itemsInUserCart = itemInCartRepository.findAllByUser(user);
        if (itemsInUserCart.isEmpty()) {
            for (var item : itemInCarts) {
                item.setUser(user);
                item.setId(null);
                this.itemInCartRepository.save(item);
            }
            return;
        }
        for (ItemInCart itemToAdd : itemInCarts) {
            boolean itemExists = false;

            for (ItemInCart userItem : itemsInUserCart) {
                if (userItem.getItem().getId().equals(itemToAdd.getItem().getId())) {
                    userItem.setQuantity(userItem.getQuantity() + itemToAdd.getQuantity());
                    itemExists = true;
                    break;
                }
            }
            if (!itemExists) {
                itemToAdd.setUser(user);
                itemToAdd.setId(null);
                itemsInUserCart.add(itemToAdd);
            }
        }
        itemInCartRepository.saveAll(itemsInUserCart);
    }
}
