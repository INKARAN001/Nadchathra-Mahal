package com.example.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "Food")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    @Column(nullable = false)
    private String foodName;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    @Builder.Default
    private boolean available = true;

    @ManyToMany(mappedBy = "foodItems")
    private List<Booking> bookings;
}
