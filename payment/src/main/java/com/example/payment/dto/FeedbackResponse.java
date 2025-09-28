package com.example.payment.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponse {

    private Long feedbackId;
    private String customerName;
    private String email;
    private int rating;
    private String category;
    private String comments;
    private String suggestions;
    private LocalDateTime createdAt;
}
