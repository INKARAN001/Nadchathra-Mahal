package com.gsc.hallbooking.config;

import com.gsc.hallbooking.entity.Admin;
import com.gsc.hallbooking.entity.FoodItem;
import com.gsc.hallbooking.entity.Hall;
import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.repository.AdminRepository;
import com.gsc.hallbooking.repository.FoodItemRepository;
import com.gsc.hallbooking.repository.HallRepository;
import com.gsc.hallbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DEVELOPER: JADAVAN
 * CRUD: DataInitializer - Database Initialization
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final HallRepository hallRepository;
    private final FoodItemRepository foodItemRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Create default admin user if not exists
        if (!userRepository.existsByEmail("admin@gsc.com")) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@gsc.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("1234567890")
                    .role("ADMIN")
                    .enabled(true)
                    .build();
            admin = userRepository.save(admin);
            
            // Create admin record for this user
            Admin adminRecord = Admin.builder()
                    .user(admin)
                    .userStatus("ACTIVE")
                    .totalBookings(0)
                    .build();
            adminRepository.save(adminRecord);
            
            System.out.println("Default admin user created: admin@gsc.com / admin123");
            System.out.println("Admin record created for default admin user");
        }
        
        // Create default manager user if not exists
        if (!userRepository.existsByEmail("manager@gsc.com")) {
            User manager = User.builder()
                    .name("Manager")
                    .email("manager@gsc.com")
                    .password(passwordEncoder.encode("manager123"))
                    .phone("9876543210")
                    .role("MANAGER")
                    .enabled(true)
                    .build();
            userRepository.save(manager);
            
            System.out.println("Default manager user created: manager@gsc.com / manager123");
        }
        
        // Create default halls if not exist
        createDefaultHalls();
        
        // Create default food items if not exist
        createDefaultFoodItems();
    }
    
    private void createDefaultHalls() {
        // Hall 1
        if (!hallRepository.existsByName("Hall 1")) {
            Hall hall1 = Hall.builder()
                    .name("Hall 1")
                    .capacity(800)
                    .description("Perfect for medium-sized events")
                    .active(true)
                    .build();
            hallRepository.save(hall1);
            System.out.println("Default hall created: Hall 1");
        }
        
        // Hall 2
        if (!hallRepository.existsByName("Hall 2")) {
            Hall hall2 = Hall.builder()
                    .name("Hall 2")
                    .capacity(800)
                    .description("Ideal for large gatherings")
                    .active(true)
                    .build();
            hallRepository.save(hall2);
            System.out.println("Default hall created: Hall 2");
        }
        
        // Garden Venue (Outdoor)
        if (!hallRepository.existsByName("Garden Venue (Outdoor)")) {
            Hall gardenHall = Hall.builder()
                    .name("Garden Venue (Outdoor)")
                    .capacity(800)
                    .description("Beautiful outdoor setting with natural ambiance")
                    .active(true)
                    .build();
            hallRepository.save(gardenHall);
            System.out.println("Default hall created: Garden Venue (Outdoor)");
        }
    }
    
    private void createDefaultFoodItems() {
        // Green Delight (Veg)
        if (!foodItemRepository.existsByName("Green Delight")) {
            FoodItem greenDelight = FoodItem.builder()
                    .name("Green Delight")
                    .pricePerPerson(500.0)
                    .description("Fresh vegetarian menu with local specialties")
                    .category("VEG")
                    .active(true)
                    .build();
            foodItemRepository.save(greenDelight);
            System.out.println("Default food item created: Green Delight");
        }
        
        // Royal Feast (Non-Veg)
        if (!foodItemRepository.existsByName("Royal Feast")) {
            FoodItem royalFeast = FoodItem.builder()
                    .name("Royal Feast")
                    .pricePerPerson(800.0)
                    .description("Premium non-vegetarian buffet with exotic dishes")
                    .category("NON_VEG")
                    .active(true)
                    .build();
            foodItemRepository.save(royalFeast);
            System.out.println("Default food item created: Royal Feast");
        }
        
        // Spicy Grill (Chicken)
        if (!foodItemRepository.existsByName("Spicy Grill")) {
            FoodItem spicyGrill = FoodItem.builder()
                    .name("Spicy Grill")
                    .pricePerPerson(750.0)
                    .description("Chicken specialties with spicy marinades")
                    .category("NON_VEG")
                    .active(true)
                    .build();
            foodItemRepository.save(spicyGrill);
            System.out.println("Default food item created: Spicy Grill");
        }
        
        // Ocean Treasure (Seafood)
        if (!foodItemRepository.existsByName("Ocean Treasure")) {
            FoodItem oceanTreasure = FoodItem.builder()
                    .name("Ocean Treasure")
                    .pricePerPerson(1000.0)
                    .description("Fresh seafood deluxe with premium fish and prawns")
                    .category("SEAFOOD")
                    .active(true)
                    .build();
            foodItemRepository.save(oceanTreasure);
            System.out.println("Default food item created: Ocean Treasure");
        }
    }
}

