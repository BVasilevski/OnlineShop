package org.example.onlineshop.controller;

import jakarta.servlet.http.HttpSession;
import org.example.onlineshop.model.*;
import org.example.onlineshop.model.enumerations.Category;
import org.example.onlineshop.repository.ItemInCartRepository;
import org.example.onlineshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemInCartService itemInCartService;
    private final UserService userService;
    private final OrderService orderService;
    private final ItemRatingService itemRatingService;

    public ItemController(ItemService itemService, ItemInCartService itemInCartService, UserService userService, OrderService orderService, ItemRatingService itemRatingService) {
        this.itemService = itemService;
        this.itemInCartService = itemInCartService;
        this.userService = userService;
        this.orderService = orderService;
        this.itemRatingService = itemRatingService;
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

    @GetMapping("/checkout")
    public String placeOrder(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<ItemInCart> items = itemInCartService.getAll();
        List<Item> orderedItems = items.stream().filter(item -> item.getUser().getUsername().equals(user.getUsername())).map(ItemInCart::getItem).toList();
        double totalPrice = items.stream().mapToDouble(item -> item.getItem().getPrice() * item.getQuantity()).sum();
        Order order = new Order((float) totalPrice, user, orderedItems);
        this.orderService.saveOrder(order);
        return "redirect:/orders";
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
        this.itemInCartService.removeFromUserCart(user, itemId);
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

}

