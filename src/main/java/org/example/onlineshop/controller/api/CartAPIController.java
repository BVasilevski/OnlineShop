package org.example.onlineshop.controller.api;

import org.example.onlineshop.model.dto.ItemInCartDTO;
import org.example.onlineshop.service.ItemInCartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartAPIController {
    private final ItemInCartService itemInCartService;

    public CartAPIController(ItemInCartService itemInCartService) {
        this.itemInCartService = itemInCartService;
    }

    @GetMapping
    public ResponseEntity<?> getAllItemsInCartForUser(@RequestParam(name = "userId") Long userId) {
        try {
            List<ItemInCartDTO> itemInCarts = this.itemInCartService.getAllItemsInUserCartDTO(userId);
            return ResponseEntity.ok(itemInCarts);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PostMapping("/add_to_cart")
    public ResponseEntity<?> addItemToUserCart(@RequestParam(name = "userId") Long userId,
                                               @RequestParam(name = "itemId") Long itemId,
                                               @RequestParam(name = "quantity") Integer quantity) {
        try {
            itemInCartService.addItemToUserCart(userId, itemId, quantity);
            return ResponseEntity.ok("Item added to cart successfully");
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/remove_from_cart")
    public ResponseEntity<?> removeItemFromUserCart(@RequestParam(name = "itemId") Long itemId) {
        try {
            this.itemInCartService.removeFromUserCart(itemId);
            return ResponseEntity.ok("Item deleted from cart successfully");
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
