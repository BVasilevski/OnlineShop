package org.example.onlineshop.service;

import org.example.onlineshop.model.ItemRating;
import org.example.onlineshop.repository.ItemRatingRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemRatingService {
    private final ItemRatingRepository itemRatingRepository;

    public ItemRatingService(ItemRatingRepository itemRatingRepository) {
        this.itemRatingRepository = itemRatingRepository;
    }

    public void save(ItemRating itemRating) {
        this.itemRatingRepository.save(itemRating);
    }
}