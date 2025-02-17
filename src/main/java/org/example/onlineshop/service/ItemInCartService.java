package org.example.onlineshop.service;

import jakarta.transaction.Transactional;
import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.ItemInCart;
import org.example.onlineshop.model.User;
import org.example.onlineshop.model.dto.ItemInCartDTO;
import org.example.onlineshop.repository.ItemInCartRepository;
import org.example.onlineshop.repository.ItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemInCartService {
    private final ItemInCartRepository itemInCartRepository;
    private final UserService userService;

    private final ItemRepository itemRepository;

    public ItemInCartService(ItemInCartRepository itemInCartRepository, UserService userService, ItemRepository itemRepository) {
        this.itemInCartRepository = itemInCartRepository;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    public List<ItemInCart> getAllItemsInUserCart(User user) {
        return itemInCartRepository.findAllByUser(user);
    }

    public List<ItemInCartDTO> getAllItemsInUserCartDTO(Long userId) {
        User user = userService.findById(userId);
        return itemInCartRepository.findAllByUser(user)
                .stream()
                .map(itemInCart -> new ItemInCartDTO(
                        itemInCart.getId(),
                        itemInCart.getItem().getName(),
                        itemInCart.getItem().getImageUrl(),
                        itemInCart.getItem().getPrice(),
                        itemInCart.getQuantity()
                ))
                .collect(Collectors.toList());
    }


    public List<ItemInCart> getAll() {
        Sort sort = Sort.by("quantity").descending();
        return itemInCartRepository.findAll(sort);
    }

    @Transactional
    public void save(ItemInCart itemInCart) {
        itemInCartRepository.save(itemInCart);
    }

    @Transactional
    public void removeFromUserCart(Long itemId) {
        ItemInCart item = this.itemInCartRepository.findById(itemId).orElseThrow(() -> new RuntimeException(String.format("Item with id %d doesn't exist", itemId)));
        this.itemInCartRepository.delete(item);
    }

    @Transactional
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

    public void addItemToUserCart(Long userId, Long itemId, Integer quantity) {
        User user = userService.findById(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException(String.format("Item with id %d doesn't exist", itemId)));
        ItemInCart itemInCart = new ItemInCart(user, item, quantity);
        itemInCartRepository.save(itemInCart);
    }
}
