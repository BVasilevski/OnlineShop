package org.example.onlineshop.service;

import org.example.onlineshop.model.ShopRating;
import org.example.onlineshop.repository.ShopRatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopRatingService {
    private final ShopRatingRepository shopRatingRepository;

    public ShopRatingService(ShopRatingRepository shopRatingRepository) {
        this.shopRatingRepository = shopRatingRepository;
    }

    public void save(ShopRating shopRating) {
        this.shopRatingRepository.save(shopRating);
    }

    public List<ShopRating> findAll() {
        return this.shopRatingRepository.findAll();
    }
}
