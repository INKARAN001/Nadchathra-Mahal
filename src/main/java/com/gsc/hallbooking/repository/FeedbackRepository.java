package com.gsc.hallbooking.repository;

import com.gsc.hallbooking.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByApprovedTrue();
    List<Feedback> findAllByOrderByCreatedAtDesc();
}

