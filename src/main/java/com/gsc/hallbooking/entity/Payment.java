package com.gsc.hallbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod; // CARD or CASH
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(nullable = false, length = 20)
    private String status; // PENDING, COMPLETED, FAILED
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
    }
}

