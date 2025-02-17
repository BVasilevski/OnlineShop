package org.example.onlineshop.configurations;

import org.example.onlineshop.service.ItemService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    private final ItemService itemService;

    public DataInitializer(ItemService itemService) {
        this.itemService = itemService;
    }

//    @PostConstruct
//    public void initData() {
//        for (int i = 0; i < 3; i++) {
//            Item item = new Item("", (i + 1) * 20, "Item " + i, Category.CPU, LocalDate.now(), 5);
//            itemService.save(item);
//        }
//        for (int i = 0; i < 3; i++) {
//            Item item = new Item("", (i + 1) * 20, "Item " + i, Category.GPU, LocalDate.now(), 5);
//            itemService.save(item);
//        }
//        for (int i = 0; i < 3; i++) {
//            Item item = new Item("", (i + 1) * 20, "Item " + i, Category.MOTHERBOARD, LocalDate.now(), 5);
//            itemService.save(item);
//        }
//    }
}
