package com.gsc.hallbooking.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * DEVELOPER: SRIKARSAN
 * CRUD: Hall Entity - Hall Management
 */
@Entity
@Table(name = "halls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Boolean active;
    
    @PrePersist
    protected void onCreate() {
        if (active == null) {
            active = true;
        }
    }
}

