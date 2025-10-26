package com.gsc.hallbooking.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * DEVELOPER: AKASH
 * CRUD: BookingDetails Entity - Booking Details Management
 */
@Entity
@Table(name = "booking_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;
    
    @Column(nullable = false)
    private String foodType; // Green Delight, Royal Feast, Spicy Grill, Ocean Treasure
    
    @Column(nullable = false)
    private Integer peopleCount;
    
    @Column(nullable = false)
    private String paymentType; // CARD or CASH
    
    @Column(nullable = false)
    private Double totalAmount;
    
    @Column(nullable = false)
    private String paymentStatus; // PENDING, PAID, CANCELLED
    
    @Column(nullable = false)
    private String bookingStatus; // PENDING, ACCEPTED, REJECTED, BOOKED
}

