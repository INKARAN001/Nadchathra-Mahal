package com.gsc.hallbooking.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * DEVELOPER: SRIKARSAN
 * CRUD: FoodItem Entity - Food Item Management
 */
@Entity
@Table(name = "food_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private Double pricePerPerson;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private String category; // VEG, NON_VEG, SEAFOOD, etc.
    
    @Column(nullable = false)
    private Boolean active;
    
    @PrePersist
    protected void onCreate() {
        if (active == null) {
            active = true;
        }
    }
}

