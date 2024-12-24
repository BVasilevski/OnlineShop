package org.example.onlineshop.repository;

import org.example.onlineshop.model.ShopRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRatingRepository extends JpaRepository<ShopRating, Long> {
}
