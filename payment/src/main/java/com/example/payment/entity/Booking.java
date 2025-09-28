package com.example.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String hall;

    @Column(nullable = false)
    private int expectedGuests;

    @Column(nullable = false)
    private LocalDate eventDate;

    private double totalPrice;

    @Column(nullable = false)
    @Builder.Default
    private String status = "Pending";

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    @ManyToMany
    @JoinTable(
        name = "Booking_Food",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> foodItems;
}
