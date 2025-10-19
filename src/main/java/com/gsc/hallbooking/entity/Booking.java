package com.gsc.hallbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
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
    
    @Column(nullable = false)
    private String foodType; // Green Delight, Royal Feast, Spicy Grill, Ocean Treasure
    
    @Column(nullable = false)
    private Integer peopleCount;
    
    @Column(nullable = false)
    private Double totalAmount;
    
    @Column(nullable = false)
    private String paymentType; // CARD or CASH
    
    @Column(nullable = false)
    private String paymentStatus; // PENDING, PAID
    
    @Column(nullable = false)
    private String bookingStatus; // PENDING, ACCEPTED, REJECTED, BOOKED
    
    // Removed bidirectional relationship to avoid circular dependency
    // Payment relationship is handled unidirectionally from Payment entity
}

