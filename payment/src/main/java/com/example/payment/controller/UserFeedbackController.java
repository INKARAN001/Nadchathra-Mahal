package com.example.payment.controller;

import com.example.payment.entity.UserFeedback;
import com.example.payment.respository.UserFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-feedback")
@CrossOrigin(origins = "*")
public class UserFeedbackController {

    @Autowired
    private UserFeedbackRepository userFeedbackRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> submitFeedback(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Integer rating = Integer.valueOf(request.get("rating").toString());
            
            UserFeedback feedback = new UserFeedback(userId, rating);
            userFeedbackRepository.save(feedback);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Thank you for your feedback!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to submit feedback");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserFeedback>> getAllFeedback() {
        try {
            List<UserFeedback> feedbacks = userFeedbackRepository.findAllByOrderByCreatedAtDesc();
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getFeedbackStats() {
        try {
            long totalFeedbacks = userFeedbackRepository.count();
            Double averageRating = userFeedbackRepository.findAverageRating();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalFeedbacks", totalFeedbacks);
            stats.put("averageRating", averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateFeedback(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            UserFeedback feedback = userFeedbackRepository.findById(id).orElse(null);
            if (feedback == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Feedback not found");
                return ResponseEntity.notFound().build();
            }
            
            Integer rating = Integer.valueOf(request.get("rating").toString());
            feedback.setRating(rating);
            userFeedbackRepository.save(feedback);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Feedback updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update feedback");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteFeedback(@PathVariable Long id) {
        try {
            userFeedbackRepository.deleteById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Feedback deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete feedback");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
