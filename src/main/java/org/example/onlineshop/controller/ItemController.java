package org.example.onlineshop.controller;

import jakarta.servlet.http.HttpSession;
import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.ItemInCart;
import org.example.onlineshop.model.User;
import org.example.onlineshop.model.enumerations.Category;
import org.example.onlineshop.repository.ItemInCartRepository;
import org.example.onlineshop.service.ItemInCartService;
import org.example.onlineshop.service.ItemService;
import org.example.onlineshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemInCartService itemInCartService;
    private final UserService userService;

    public ItemController(ItemService itemService, ItemInCartService itemInCartService, UserService userService) {
        this.itemService = itemService;
        this.itemInCartService = itemInCartService;
        this.userService = userService;
    }

    @GetMapping
    public String listItems(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        model.addAttribute("categories", Category.values());
        return "item-list";
    }

    @GetMapping("/new")
    public String showItemForm(Model model) {
        List<Category> categories = List.of(Category.values());
        model.addAttribute("categories", categories);
        return "item-form";
    }

    @PostMapping("/save")
    public String saveItem(@RequestParam("name") String name,
                           @RequestParam("imageUrl") String imageUrl,
                           @RequestParam("price") float price,
                           @RequestParam("category") String category) {

        Item item = new Item(imageUrl, price, name, Category.valueOf(category));
        itemService.save(item);

        return "redirect:/items";
    }


    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
        return "redirect:/items";
    }

    @GetMapping("/cart")
    public String getItemsInCart(HttpSession session,
                                 Model model) {
        if (session.getAttribute("user") == null) {
            User user = new User();
            session.setAttribute("user", user);
        }

        User user = (User) session.getAttribute("user");
        // koshnickata e vo sesija
        if (!userService.exists(user.getUsername())) {
            List<ItemInCart> itemInCarts = (List<ItemInCart>) session.getAttribute("itemInCarts");
            model.addAttribute("itemsInCart", itemInCarts);
        } else {
            List<ItemInCart> items = itemInCartService.getAll();
            items = items.stream().filter(item -> item.getUser().getUsername().equals(user.getUsername())).toList();
            model.addAttribute("itemsInCart", items);
        }
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long itemId,
                            HttpSession session) {
        User user;
        if (session.getAttribute("user") == null) {
            user = new User();
            session.setAttribute("user", user);
        }
        user = (User) session.getAttribute("user");
        if (!userService.exists(user.getUsername())) {
            List<ItemInCart> itemInCarts;
            if (session.getAttribute("itemInCarts") == null) {
                itemInCarts = new ArrayList<>();
                session.setAttribute("itemInCarts", itemInCarts);
            }
            ItemInCart itemInCart = new ItemInCart(user, itemService.findById(itemId), 1);
            itemInCarts = (List<ItemInCart>) session.getAttribute("itemInCarts");
            itemInCarts.add(itemInCart);
            session.setAttribute("itemInCarts", itemInCarts);
        } else {
            itemService.addToUserCart(itemId, user, 1);
        }

        return "redirect:/items/cart";
    }


    @GetMapping("/filter")
    public String filterItems(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
            Model model) {

        List<Item> filteredItems = itemService.getItemsFiltered(
                name, category, minPrice, maxPrice, minRating, maxRating, sortDirection);

        model.addAttribute("items", filteredItems);
        model.addAttribute("categories", Category.values());
        return "item-list";
    }

}

