package com.example.payment.respository;

import com.example.payment.entity.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFeedbackRepository extends JpaRepository<UserFeedback, Long> {
    
    // Find all feedback ordered by creation date
    List<UserFeedback> findAllByOrderByCreatedAtDesc();
    
    // Calculate average rating
    @Query("SELECT AVG(uf.rating) FROM UserFeedback uf WHERE uf.rating IS NOT NULL")
    Double findAverageRating();
    
    long count();
}
