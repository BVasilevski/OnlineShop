package org.example.onlineshop.repository;

import org.example.onlineshop.model.ItemInCart;
import org.example.onlineshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemInCartRepository extends JpaRepository<ItemInCart, Long> {
    List<ItemInCart> findAllByUser(User user);
}
