package org.example.onlineshop.service;

import jakarta.persistence.criteria.Predicate;
import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.ItemInCart;
import org.example.onlineshop.model.ItemRating;
import org.example.onlineshop.model.User;
import org.example.onlineshop.model.dto.ItemDTO;
import org.example.onlineshop.model.enumerations.Category;
import org.example.onlineshop.repository.ItemRatingRepository;
import org.example.onlineshop.repository.ItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemInCartService itemInCartService;
    private final ItemRatingRepository itemRatingRepository;

    public ItemService(ItemRepository productRepository, ItemInCartService itemInCartService, ItemRatingRepository itemRatingRepository) {
        this.itemRepository = productRepository;
        this.itemInCartService = itemInCartService;
        this.itemRatingRepository = itemRatingRepository;
    }

    // Save or update product
    public void save(Item item) {
        itemRepository.save(item);
    }

    // Find all products
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    // Find product by ID
    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Delete product by ID
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    public void addToUserCart(Long itemId, User user, int quantity) {
        Item item = findById(itemId);
        List<ItemInCart> userCart = itemInCartService.getAllItemsInUserCart(user);

        for (var itemInCart : userCart) {
            if (itemInCart.getItem().equals(item)) {
                itemInCart.setQuantity(itemInCart.getQuantity() + quantity);
                itemInCartService.save(itemInCart);
                return;
            }
        }

        ItemInCart itemInCart = new ItemInCart(user, item, quantity);
        itemInCartService.save(itemInCart);
    }

    public Specification<Item> buildSpecification(String name, String category, Double minPrice, Double maxPrice, Double minRating, Double maxRating) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // Filter by name
            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // Filter by category
            if (category != null && !category.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("category"), category));
            }

            // Filter by price range
            if (minPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            // Filter by rating range
            if (minRating != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("avgRating"), minRating));
            }
            if (maxRating != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("avgRating"), maxRating));
            }

            return predicate;
        };
    }


    public List<Item> getItemsFiltered(String name, String category, Double minPrice, Double maxPrice,
                                       Double minRating, Double maxRating, String sortDirection) {
        Specification<Item> specification = buildSpecification(name, category, minPrice, maxPrice, minRating, maxRating);

        Sort sort = Sort.by("price");
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        }

        return itemRepository.findAll(specification, sort);
    }

    public void update(Long itemId, String name, String imageUrl, Float price, Category category, LocalDate date, int quantity) {
        Item item = this.findById(itemId);
        item.setName(name);
        item.setImageUrl(imageUrl);
        item.setPrice(price);
        item.setCategory(category);
        item.setDateCreated(date);
        item.setQuantity(quantity);
        this.itemRepository.save(item);
    }

    public void calculateAvgRating(Item item) {
        List<ItemRating> ratingsForItem = this.itemRatingRepository.findAllByItem(item);
        double avgRating = ratingsForItem.stream().mapToDouble(ItemRating::getRating).average().getAsDouble();
        item.setAvgRating((float) avgRating);
        itemRepository.save(item);
    }

    public List<Item> getNewItems() {
        List<Item> allItems = findAll();
        LocalDate after = LocalDate.now().minusDays(7);
        return this.itemRepository.findAllByDateCreatedAfter(after);
    }

    public List<Item> findByCategory(String category) {
        return itemRepository.findByCategory(Category.valueOf(category));
    }

    public ItemDTO findByIdDTO(Long itemId) {
        Item item = this.findById(itemId);
        return new ItemDTO(item.getId(), item.getName(), item.getPrice(), item.getImageUrl());
    }
}
