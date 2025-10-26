package com.gsc.hallbooking.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * DEVELOPER: INKARAN
 * CRUD: Admin Entity - Admin Management
 */
@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(name = "user_status", nullable = false, length = 20)
    private String userStatus;
    
    @Column(name = "total_bookings", nullable = false)
    private Integer totalBookings;
    
    @PrePersist
    protected void onCreate() {
        if (userStatus == null) {
            userStatus = "ACTIVE";
        }
        if (totalBookings == null) {
            totalBookings = 0;
        }
    }
}

