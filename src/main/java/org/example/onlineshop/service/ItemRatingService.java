package org.example.onlineshop.service;

import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.ItemRating;
import org.example.onlineshop.repository.ItemRatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemRatingService {
    private final ItemRatingRepository itemRatingRepository;
    private final ItemService itemService;

    public ItemRatingService(ItemRatingRepository itemRatingRepository, ItemService itemService) {
        this.itemRatingRepository = itemRatingRepository;
        this.itemService = itemService;
    }

    public void save(ItemRating itemRating) {
        this.itemRatingRepository.save(itemRating);
        this.itemService.calculateAvgRating(itemRating.getItem());
    }

    public List<ItemRating> findReviewsForItem(Item item) {
        return this.itemRatingRepository.findAllByItem(item);
    }
}
