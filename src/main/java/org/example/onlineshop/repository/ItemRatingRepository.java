package org.example.onlineshop.repository;

import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.ItemRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRatingRepository extends JpaRepository<ItemRating, Long> {
    List<ItemRating> findAllByItem(Item item);
}
