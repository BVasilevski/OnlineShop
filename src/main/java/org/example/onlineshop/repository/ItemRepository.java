package org.example.onlineshop.repository;

import org.example.onlineshop.model.Item;
import org.example.onlineshop.model.enumerations.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAll(Specification<Item> specification, Sort sort);

    List<Item> findAllByDateCreatedAfter(LocalDate date);

    List<Item> findByCategory(Category category);
}
