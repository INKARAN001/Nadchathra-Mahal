package com.example.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Event name is required")
    private String eventName;

    @NotBlank(message = "Hall is required")
    private String hall;

    @NotNull(message = "Expected guests is required")
    @Min(value = 1, message = "There must be at least 1 guest")
    private Integer expectedGuests;

    @NotBlank(message = "Event date is required (yyyy-MM-dd)")
    private String eventDate;  // sent from frontend as string

    private List<Long> foodIds; // IDs of selected food items
}
