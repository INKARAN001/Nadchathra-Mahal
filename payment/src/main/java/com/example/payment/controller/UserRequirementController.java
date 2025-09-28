package com.example.payment.controller;

import com.example.payment.dto.UserRequirementDTO;
import com.example.payment.entity.UserRequirement;
import com.example.payment.service.UserRequirementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-requirements")
@CrossOrigin(origins = "*")
@Validated
public class UserRequirementController {

    private final UserRequirementService userRequirementService;

    public UserRequirementController(UserRequirementService userRequirementService) {
        this.userRequirementService = userRequirementService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUserRequirement(@Valid @RequestBody UserRequirementDTO dto) {
        try {
            UserRequirement saved = userRequirementService.saveUserRequirement(dto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "User requirements saved successfully");
            response.put("id", saved.getId());
            response.put("data", saved);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Failed to save user requirements: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserRequirement>> getAllUserRequirements() {
        try {
            List<UserRequirement> requirements = userRequirementService.getAllUserRequirements();
            return ResponseEntity.ok(requirements);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRequirement>> getUserRequirementsByUserId(@PathVariable Long userId) {
        try {
            List<UserRequirement> requirements = userRequirementService.getUserRequirementsByUserId(userId);
            return ResponseEntity.ok(requirements);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequirement> getUserRequirementById(@PathVariable Long id) {
        try {
            Optional<UserRequirement> requirement = userRequirementService.getUserRequirementById(id);
            return requirement.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<UserRequirement>> getUserRequirementsByEmail(@PathVariable String email) {
        try {
            List<UserRequirement> requirements = userRequirementService.getUserRequirementsByEmail(email);
            return ResponseEntity.ok(requirements);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/event-type/{eventType}")
    public ResponseEntity<List<UserRequirement>> getUserRequirementsByEventType(@PathVariable String eventType) {
        try {
            List<UserRequirement> requirements = userRequirementService.getUserRequirementsByEventType(eventType);
            return ResponseEntity.ok(requirements);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/payment-status/{status}")
    public ResponseEntity<List<UserRequirement>> getUserRequirementsByPaymentStatus(@PathVariable String status) {
        try {
            List<UserRequirement> requirements = userRequirementService.getUserRequirementsByPaymentStatus(status);
            return ResponseEntity.ok(requirements);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/payment-status")
    public ResponseEntity<Map<String, Object>> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String paymentStatus,
            @RequestParam(required = false) Long transactionId) {
        try {
            UserRequirement updated = userRequirementService.updatePaymentStatus(id, paymentStatus, transactionId);
            
            if (updated != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "SUCCESS");
                response.put("message", "Payment status updated successfully");
                response.put("data", updated);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "ERROR");
                errorResponse.put("message", "User requirement not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Failed to update payment status: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserRequirementStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            long total = userRequirementService.getTotalUserRequirements();
            long weddings = userRequirementService.getCountByEventType("Wedding");
            long birthdays = userRequirementService.getCountByEventType("Birthday");
            long conferences = userRequirementService.getCountByEventType("Conference");
            long corporate = userRequirementService.getCountByEventType("Corporate");
            long other = userRequirementService.getCountByEventType("Other");
            
            long pending = userRequirementService.getCountByPaymentStatus("PENDING");
            long completed = userRequirementService.getCountByPaymentStatus("COMPLETED");
            long failed = userRequirementService.getCountByPaymentStatus("FAILED");
            
            stats.put("total", total);
            stats.put("byEventType", Map.of(
                "Wedding", weddings,
                "Birthday", birthdays,
                "Conference", conferences,
                "Corporate", corporate,
                "Other", other
            ));
            stats.put("byPaymentStatus", Map.of(
                "PENDING", pending,
                "COMPLETED", completed,
                "FAILED", failed
            ));
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Failed to get statistics: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveUserRequirement(@PathVariable Long id) {
        try {
            System.out.println("Attempting to approve user requirement with ID: " + id);
            UserRequirement updated = userRequirementService.updateApprovalStatus(id, "APPROVED", null);
            
            if (updated != null) {
                System.out.println("Successfully approved user requirement: " + updated.getId());
                Map<String, Object> response = new HashMap<>();
                response.put("status", "SUCCESS");
                response.put("message", "User requirement approved successfully");
                response.put("data", updated);
                return ResponseEntity.ok(response);
            } else {
                System.out.println("User requirement not found with ID: " + id);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "ERROR");
                errorResponse.put("message", "User requirement not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error approving user requirement: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Failed to approve user requirement: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectUserRequirement(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        try {
            System.out.println("Attempting to reject user requirement with ID: " + id + ", reason: " + reason);
            UserRequirement updated = userRequirementService.updateApprovalStatus(id, "REJECTED", reason);
            
            if (updated != null) {
                System.out.println("Successfully rejected user requirement: " + updated.getId());
                Map<String, Object> response = new HashMap<>();
                response.put("status", "SUCCESS");
                response.put("message", "User requirement rejected successfully");
                response.put("data", updated);
                return ResponseEntity.ok(response);
            } else {
                System.out.println("User requirement not found with ID: " + id);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "ERROR");
                errorResponse.put("message", "User requirement not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error rejecting user requirement: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Failed to reject user requirement: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserRequirement(@PathVariable Long id) {
        try {
            userRequirementService.deleteUserRequirement(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "User requirement deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Failed to delete user requirement: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "UserRequirementController is working!");
        response.put("timestamp", java.time.LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}
