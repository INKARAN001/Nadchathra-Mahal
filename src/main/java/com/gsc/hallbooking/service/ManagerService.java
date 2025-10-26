package com.gsc.hallbooking.service;

import com.gsc.hallbooking.entity.FoodItem;
import com.gsc.hallbooking.entity.Hall;
import com.gsc.hallbooking.repository.FoodItemRepository;
import com.gsc.hallbooking.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DEVELOPER: SRIKARSAN
 * CRUD: Manager Service - Hall & Food Management
 */
@Service
@RequiredArgsConstructor
public class ManagerService {
    
    private final HallRepository hallRepository;
    private final FoodItemRepository foodItemRepository;
    
    // ========== HALL CRUD OPERATIONS ==========
    
    // READ - Get all halls
    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }
    
    // READ - Get active halls
    public List<Hall> getActiveHalls() {
        return hallRepository.findByActiveTrue();
    }
    
    // READ - Get hall by ID
    public Hall getHallById(Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hall not found"));
    }
    
    // CREATE - Create new hall
    @Transactional
    public Hall createHall(String name, Integer capacity, String description) {
        if (hallRepository.existsByName(name)) {
            throw new RuntimeException("Hall with this name already exists");
        }
        
        Hall hall = Hall.builder()
                .name(name)
                .capacity(capacity)
                .description(description)
                .active(true)
                .build();
        
        return hallRepository.save(hall);
    }
    
    // UPDATE - Update hall
    @Transactional
    public Hall updateHall(Long id, String name, Integer capacity, String description, Boolean active) {
        Hall hall = getHallById(id);
        
        // Check if name is being changed and if new name already exists
        if (!hall.getName().equals(name) && hallRepository.existsByName(name)) {
            throw new RuntimeException("Hall with this name already exists");
        }
        
        hall.setName(name);
        hall.setCapacity(capacity);
        hall.setDescription(description);
        hall.setActive(active);
        
        return hallRepository.save(hall);
    }
    
    // DELETE - Delete hall
    @Transactional
    public void deleteHall(Long id) {
        Hall hall = getHallById(id);
        hallRepository.delete(hall);
    }
    
    @Transactional
    public void toggleHallStatus(Long id) {
        Hall hall = getHallById(id);
        hall.setActive(!hall.getActive());
        hallRepository.save(hall);
    }
    
    // ========== FOOD ITEM CRUD OPERATIONS ==========
    
    // READ - Get all food items
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }
    
    // READ - Get active food items
    public List<FoodItem> getActiveFoodItems() {
        return foodItemRepository.findByActiveTrue();
    }
    
    // READ - Get food item by ID
    public FoodItem getFoodItemById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found"));
    }
    
    // CREATE - Create new food item
    @Transactional
    public FoodItem createFoodItem(String name, Double pricePerPerson, String description, String category) {
        if (foodItemRepository.existsByName(name)) {
            throw new RuntimeException("Food item with this name already exists");
        }
        
        FoodItem foodItem = FoodItem.builder()
                .name(name)
                .pricePerPerson(pricePerPerson)
                .description(description)
                .category(category)
                .active(true)
                .build();
        
        return foodItemRepository.save(foodItem);
    }
    
    // UPDATE - Update food item
    @Transactional
    public FoodItem updateFoodItem(Long id, String name, Double pricePerPerson, 
                                   String description, String category, Boolean active) {
        FoodItem foodItem = getFoodItemById(id);
        
        // Check if name is being changed and if new name already exists
        if (!foodItem.getName().equals(name) && foodItemRepository.existsByName(name)) {
            throw new RuntimeException("Food item with this name already exists");
        }
        
        foodItem.setName(name);
        foodItem.setPricePerPerson(pricePerPerson);
        foodItem.setDescription(description);
        foodItem.setCategory(category);
        foodItem.setActive(active);
        
        return foodItemRepository.save(foodItem);
    }
    
    // DELETE - Delete food item
    @Transactional
    public void deleteFoodItem(Long id) {
        FoodItem foodItem = getFoodItemById(id);
        foodItemRepository.delete(foodItem);
    }
    
    @Transactional
    public void toggleFoodItemStatus(Long id) {
        FoodItem foodItem = getFoodItemById(id);
        foodItem.setActive(!foodItem.getActive());
        foodItemRepository.save(foodItem);
    }
}

