package org.example.onlineshop.service;

import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.Order;
import org.example.onlineshop.model.enumerations.Category;
import org.example.onlineshop.repository.ItemRepository;
import org.example.onlineshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public RecommendationService(ItemRepository itemRepository, OrderRepository orderRepository) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    public List<Item> recommendItems(Long orderId) {
        if (orderId == null) {
            return getTopSellingItems();
        }
        try {
            // Fetch the order and its items
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException(String.format("Order with id %d not found", orderId)));
            List<Item> itemsInOrder = order.getItems(); // Items in the current order
            Set<Category> categoriesInOrder = itemsInOrder.stream()
                    .map(Item::getCategory)
                    .collect(Collectors.toSet());

            List<Item> recommendedItems = new ArrayList<>();

            // Step 1: Recommend based on the number of categories present in the order
            if (categoriesInOrder.size() == 1) {
                // If there is only one category, recommend the other two categories
                Category existingCategory = categoriesInOrder.iterator().next();
                if (existingCategory == Category.CPU) {
                    recommendedItems.addAll(getItemsFromCategoriesExcludingOrder(Category.RAM, itemsInOrder));
                    recommendedItems.addAll(getItemsFromCategoriesExcludingOrder(Category.MOTHERBOARD, itemsInOrder));
                } else if (existingCategory == Category.RAM) {
                    recommendedItems.addAll(getItemsFromCategoriesExcludingOrder(Category.CPU, itemsInOrder));
                    recommendedItems.addAll(getItemsFromCategoriesExcludingOrder(Category.MOTHERBOARD, itemsInOrder));
                } else {
                    recommendedItems.addAll(getItemsFromCategoriesExcludingOrder(Category.CPU, itemsInOrder));
                    recommendedItems.addAll(getItemsFromCategoriesExcludingOrder(Category.RAM, itemsInOrder));
                }
            } else if (categoriesInOrder.size() == 2) {
                // If there are two categories, recommend the third category
                if (!categoriesInOrder.contains(Category.CPU)) {
                    recommendedItems.addAll(getItemsFromCategoriesExcludingOrder(Category.CPU, itemsInOrder));
                } else if (!categoriesInOrder.contains(Category.RAM)) {
                    recommendedItems.addAll(getItemsFromCategoriesExcludingOrder(Category.RAM, itemsInOrder));
                } else {
                    recommendedItems.addAll(getItemsFromCategoriesExcludingOrder(Category.MOTHERBOARD, itemsInOrder));
                }
            }

            // Step 2: Handle cases with all categories present or no recommendations
            if (categoriesInOrder.size() == 3 || recommendedItems.isEmpty()) {
                recommendedItems = getTopSellingItems();
                recommendedItems.removeIf(itemsInOrder::contains); // Exclude items already in the order
            }

            return recommendedItems;
        } catch (Exception e) {
            throw new RuntimeException("Error generating recommendations", e);
        }
    }

    private List<Item> getItemsFromCategoriesExcludingOrder(Category category, List<Item> itemsInOrder) {
        List<Item> items = itemRepository.findByCategory(category);
        items.removeIf(itemsInOrder::contains);
        return items;
    }

    private List<Item> getTopSellingItems() {
        List<Order> allOrders = orderRepository.findAll();
        Map<Long, Integer> itemSalesCount = new HashMap<>();

        for (Order order : allOrders) {
            for (Item item : order.getItems()) {
                itemSalesCount.put(item.getId(), itemSalesCount.getOrDefault(item.getId(), 0) + 1);
            }
        }

        List<Item> allItems = itemRepository.findAll();
        allItems.sort((item1, item2) -> Integer.compare(itemSalesCount.getOrDefault(item2.getId(), 0), itemSalesCount.getOrDefault(item1.getId(), 0)));

        return allItems.stream().limit(5).collect(Collectors.toList());
    }
}
