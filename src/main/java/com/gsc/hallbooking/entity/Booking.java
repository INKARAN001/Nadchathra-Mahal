package com.gsc.hallbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * DEVELOPER: AKASH
 * CRUD: Booking Entity - Booking Management
 */
@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String emailAddress;
    
    @Column(nullable = false)
    private String hall; // Hall A, Hall B, Hall C
    
    @Column(nullable = false)
    private LocalDate bookingDate;
    
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BookingDetails bookingDetails;
    
    // Payment information is handled in booking_details table
}

