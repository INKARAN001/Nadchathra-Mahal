package com.example.payment.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    private String email;

    @Min(1)
    @Max(5)
    private int rating;

    private String category;

    @NotBlank(message = "Comments cannot be empty")
    private String comments;

    private String suggestions;
}
