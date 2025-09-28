package com.example.payment.controller;

import com.example.payment.dto.PaymentRequestDTO;
import com.example.payment.dto.PaymentResponseDTO;
import com.example.payment.service.PaymentService;
import com.example.payment.entity.PaymentTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
@Validated
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/validate")
    public ResponseEntity<PaymentResponseDTO> validatePayment(@Valid @RequestBody PaymentRequestDTO dto) {
        try {
            PaymentResponseDTO response = service.validateAndSave(dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponseDTO errorResponse = new PaymentResponseDTO();
            errorResponse.setStatus("FAILED");
            errorResponse.setMessage("Payment validation failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<PaymentTransaction>> listAll() {
        try {
            List<PaymentTransaction> transactions = service.findAll();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            List<PaymentTransaction> transactions = service.findAll();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", transactions.size());
            stats.put("completed", transactions.stream().filter(t -> "COMPLETED".equals(t.getStatus())).count());
            stats.put("failed", transactions.stream().filter(t -> "FAILED".equals(t.getStatus())).count());
            stats.put("pending", transactions.stream().filter(t -> "PENDING".equals(t.getStatus())).count());
            
            // Calculate success rate
            long totalAttempts = transactions.size();
            long successfulAttempts = transactions.stream().filter(t -> "COMPLETED".equals(t.getStatus())).count();
            double successRate = totalAttempts > 0 ? (double) successfulAttempts / totalAttempts * 100 : 0;
            stats.put("successRate", Math.round(successRate * 100.0) / 100.0);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Payment API");
        health.put("timestamp", java.time.Instant.now().toString());
        return ResponseEntity.ok(health);
    }
}
