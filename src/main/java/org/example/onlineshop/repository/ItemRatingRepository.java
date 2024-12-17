package org.example.onlineshop.repository;

import org.example.onlineshop.model.ItemRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRatingRepository extends JpaRepository<ItemRating, Long> {
}
