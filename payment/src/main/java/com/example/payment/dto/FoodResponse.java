package com.example.payment.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodResponse {

    private Long foodId;
    private String foodName;
    private double price;
    private boolean available;
}
