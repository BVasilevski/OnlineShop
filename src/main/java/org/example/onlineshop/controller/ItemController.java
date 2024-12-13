package org.example.onlineshop.controller;

import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.enumerations.Category;
import org.example.onlineshop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // CREATE: Show form to add a new item
    @GetMapping("/new")
    public String showItemForm(Model model) {
        List<Category> categories = List.of(Category.values());
        model.addAttribute("categories", categories);
        return "item-form";
    }

    // CREATE: Save the new item
    @PostMapping("/save")
    public String saveItem(@RequestParam("name") String name,
                           @RequestParam("imageUrl") String imageUrl,
                           @RequestParam("price") float price,
                           @RequestParam("category") String category) {

        Item item = new Item(imageUrl, price, name, Category.valueOf(category));
        itemService.save(item);

        return "redirect:/items"; // Redirect to item list after saving
    }

    // READ: Show all items
    @GetMapping
    public String listItems(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        return "item-list";
    }

    // DELETE: Delete an item
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
        return "redirect:/items"; // Redirect to item list after delete
    }
}

