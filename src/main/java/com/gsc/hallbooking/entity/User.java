package com.gsc.hallbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * DEVELOPER: JADAVAN
 * CRUD: User Entity - Core User Management
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String role; // USER or ADMIN
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    @PrePersist
    protected void onCreate() {
        if (enabled == null) {
            enabled = true;
        }
    }
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feedback> feedbacks;
}

