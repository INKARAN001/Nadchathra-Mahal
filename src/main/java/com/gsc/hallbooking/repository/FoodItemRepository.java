package com.gsc.hallbooking.repository;

import com.gsc.hallbooking.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    Optional<FoodItem> findByName(String name);
    List<FoodItem> findByActiveTrue();
    List<FoodItem> findByCategory(String category);
    boolean existsByName(String name);
}

