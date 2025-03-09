package org.example.onlineshop.controller.api;

import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.ItemRating;
import org.example.onlineshop.model.User;
import org.example.onlineshop.model.dto.ItemDTO;
import org.example.onlineshop.service.ItemRatingService;
import org.example.onlineshop.service.ItemService;
import org.example.onlineshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemAPIController {
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRatingService itemRatingService;

    public ItemAPIController(ItemService itemService, UserService userService, ItemRatingService itemRatingService) {
        this.itemService = itemService;
        this.userService = userService;
        this.itemRatingService = itemRatingService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getItems(@RequestParam(name = "category", required = false) String category) {
        List<Item> allItems = (category == null) ? itemService.findAll() : itemService.findByCategory(category);
        List<ItemDTO> items = allItems.stream().map(item -> new ItemDTO(item.getId(), item.getName(), item.getPrice(), item.getImageUrl(), item.getDescription(), null)).collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<?> getDetailsForItem(@PathVariable Long itemId) {
        try {
            ItemDTO item = this.itemService.findByIdDTO(itemId);
            return ResponseEntity.ok(item);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PostMapping("/rate/{itemId}")
    public ResponseEntity<?> addReviewForItem(@PathVariable Long itemId,
                                              @RequestParam Long userId,
                                              @RequestParam float rating,
                                              @RequestParam String comment,
                                              @RequestParam String userImageUrl) {
        try {
            Item item = this.itemService.findById(itemId);
            User user = this.userService.findById(userId);
            ItemRating itemRating = new ItemRating(item, user, rating, comment, userImageUrl);
            this.itemRatingService.save(itemRating);
            return ResponseEntity.ok("Item rating added successfully");
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/delete_rating/{ratingId}")
    public ResponseEntity<?> deleteReviewForItem(@PathVariable Long ratingId) {
        try {
            this.itemRatingService.deleteRating(ratingId);
            return ResponseEntity.ok("Item rating successfully deleted");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
