package com.example.payment.respository;

import com.example.payment.entity.UserRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequirementRepository extends JpaRepository<UserRequirement, Long> {
    
    // Find all user requirements ordered by creation date (newest first)
    List<UserRequirement> findAllByOrderByCreatedAtDesc();
    
    // Find user requirements by user ID
    List<UserRequirement> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Find user requirements by email
    List<UserRequirement> findByEmailOrderByCreatedAtDesc(String email);
    
    // Find user requirements by event type
    List<UserRequirement> findByEventTypeOrderByCreatedAtDesc(String eventType);
    
    // Find user requirements by payment status
    List<UserRequirement> findByPaymentStatusOrderByCreatedAtDesc(String paymentStatus);
    
    // Count user requirements by event type
    long countByEventType(String eventType);
    
    // Count user requirements by payment status
    long countByPaymentStatus(String paymentStatus);
}
