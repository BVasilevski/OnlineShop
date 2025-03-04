package org.example.onlineshop.repository;

import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.ItemInCart;
import org.example.onlineshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemInCartRepository extends JpaRepository<ItemInCart, Long> {
    List<ItemInCart> findAllByUser(User user);

    void deleteItemInCartByItemAndUser(Item item, User user);

    void deleteItemInCartsByIdIn(List<Long> ids);

    Optional<ItemInCart> findByUserIdAndItemId(Long userId, Long itemId);
}
