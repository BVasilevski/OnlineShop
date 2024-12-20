package org.example.onlineshop.controller;

import jakarta.servlet.http.HttpSession;
import org.example.onlineshop.model.*;
import org.example.onlineshop.model.enumerations.Category;
import org.example.onlineshop.model.enumerations.UserType;
import org.example.onlineshop.service.ItemInCartService;
import org.example.onlineshop.service.ItemRatingService;
import org.example.onlineshop.service.ItemService;
import org.example.onlineshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemInCartService itemInCartService;
    private final UserService userService;
    private final ItemRatingService itemRatingService;

    public ItemController(ItemService itemService, ItemInCartService itemInCartService, UserService userService, ItemRatingService itemRatingService) {
        this.itemService = itemService;
        this.itemInCartService = itemInCartService;
        this.userService = userService;
        this.itemRatingService = itemRatingService;
    }

    @GetMapping({"", "/"})
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

    @GetMapping("/cart")
    public String getItemsInCart(HttpSession session,
                                 Model model) {
        if (session.getAttribute("user") == null) {
            User user = new User(UserType.TEMPORARY);
            session.setAttribute("user", user);
        }

        User user = (User) session.getAttribute("user");
        // koshnickata e vo sesija
        if (user.getType().equals(UserType.TEMPORARY)) {
            List<ItemInCart> itemInCarts = (List<ItemInCart>) session.getAttribute("itemInCarts");
            float totalPrice;
            if (itemInCarts == null) {
                totalPrice = 0.0F;
            } else {
                totalPrice = (float) itemInCarts.stream().mapToDouble(item -> item.getQuantity() * item.getItem().getPrice()).sum();
            }
            model.addAttribute("itemsInCart", itemInCarts);
            model.addAttribute("totalPrice", totalPrice);
        } else {
            List<ItemInCart> items = itemInCartService.getAll();
            items = items.stream().filter(item -> item.getUser().getUsername().equals(user.getUsername())).toList();
            float totalPrice = (float) items.stream().mapToDouble(item -> item.getQuantity() * item.getItem().getPrice()).sum();
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("itemsInCart", items);
        }
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long itemId,
                            HttpSession session) {
        User user;
        if (session.getAttribute("user") == null) {
            user = new User(UserType.TEMPORARY);
            session.setAttribute("user", user);
        }
        user = (User) session.getAttribute("user");
        if (user.getType().equals(UserType.TEMPORARY)) {
            Random random = new Random();
            List<ItemInCart> itemInCarts;
            if (session.getAttribute("itemInCarts") == null) {
                itemInCarts = new ArrayList<>();
                session.setAttribute("itemInCarts", itemInCarts);
            }
            ItemInCart itemInCart = new ItemInCart(random.nextLong(), user, itemService.findById(itemId), 1);
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

    @GetMapping("/checkout")
    public String placeOrder(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<ItemInCart> items = itemInCartService.getAll();
        List<Item> orderedItems = items.stream().filter(item -> item.getUser().getUsername().equals(user.getUsername())).map(ItemInCart::getItem).toList();
        double totalPrice = items.stream().mapToDouble(item -> item.getItem().getPrice() * item.getQuantity()).sum();
        float amount = (float) (totalPrice - (totalPrice * user.getDiscount()));
        Order order = new Order(amount, user, orderedItems);
        session.setAttribute("order", order);
        model.addAttribute("amount", amount);
        return "payment-page";
    }

    @PostMapping("/review")
    public String addReviewForItem(@RequestParam Long itemId,
                                   @RequestParam String comment,
                                   @RequestParam Float rating,
                                   HttpSession session) {
        Item item = itemService.findById(itemId);
        User user = (User) session.getAttribute("user");
        ItemRating itemRating = new ItemRating(item, user, rating, comment);
        this.itemRatingService.save(itemRating);
        return "redirect:/items";
    }

    @PostMapping("/cart/delete")
    public String removeItemFromCart(@RequestParam Long itemId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getType().equals(UserType.TEMPORARY)) {
            List<ItemInCart> itemInCarts = (List<ItemInCart>) session.getAttribute("itemInCarts");
            ItemInCart itemInCart = null;
            for (ItemInCart item : itemInCarts) {
                if (item.getId().equals(itemId)) {
                    itemInCart = item;
                    break;
                }
            }
            if (itemInCart != null) {
                itemInCarts.remove(itemInCart);
            }
            session.setAttribute("itemInCarts", itemInCarts);
        } else {
            this.itemInCartService.removeFromUserCart(user, itemId);
        }
        return "redirect:/items/cart";
    }

    @GetMapping("/edit")
    public String showEditPage(@RequestParam Long itemId, Model model) {
        Item item = itemService.findById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("categories", Category.values());
        return "edit-item";
    }

    @PostMapping("/update")
    public String updateItem(@RequestParam Long itemId,
                             @RequestParam String name,
                             @RequestParam String imageUrl,
                             @RequestParam Float price,
                             @RequestParam Category category) {
        this.itemService.update(itemId, name, imageUrl, price, category);
        return "redirect:/items";
    }

    @GetMapping("/reviews/{id}")
    public String getItemReviews(@PathVariable Long id, Model model) {
        Item item = itemService.findById(id); // Fetch the item
        List<ItemRating> reviews = itemRatingService.findReviewsForItem(item);
        model.addAttribute("item", item);
        model.addAttribute("reviews", reviews);
        return "item-reviews"; // Thymeleaf template for reviews
    }

    @PostMapping("/delete")
    public String deleteItem(@RequestParam Long itemId) {
        this.itemService.delete(itemId);
        return "redirect:/items";
    }

}

