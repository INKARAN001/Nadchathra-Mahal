package com.example.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String customerName;
    private String eventName;
    private String hall;
    private int expectedGuests;
    private LocalDate eventDate;
    private double totalPrice;
    private String status;

    @Builder.Default
    private List<String> foodItems = new ArrayList<>();
}
