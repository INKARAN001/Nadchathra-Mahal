package com.example.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("transactionId")
    private Long transactionId;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    @JsonProperty("amount")
    private Double amount;
    
    // Additional fields for booking integration
    private Long paymentId;
    private Long bookingId;
    private String cardLast4;
    private String paymentStatus;
    private LocalDateTime paymentDate;

    public PaymentResponseDTO() {
        this.timestamp = LocalDateTime.now();
        this.amount = 100.00; // Fixed amount for Nadchathra Mahal
    }

    public PaymentResponseDTO(String message, String status) {
        this();
        this.message = message;
        this.status = status;
    }

    public PaymentResponseDTO(String message, String status, Long transactionId) {
        this(message, status);
        this.transactionId = transactionId;
    }
}
