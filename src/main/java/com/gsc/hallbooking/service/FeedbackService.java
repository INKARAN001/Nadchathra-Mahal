package com.gsc.hallbooking.service;

import com.gsc.hallbooking.entity.Feedback;
import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DEVELOPER: INKARAN
 * CRUD: Feedback Management System
 */
@Service
@RequiredArgsConstructor
public class FeedbackService {
    
    private final FeedbackRepository feedbackRepository;
    
    // CREATE - Create new feedback
    @Transactional
    public Feedback createFeedback(User user, String message) {
        Feedback feedback = Feedback.builder()
                .user(user)
                .message(message)
                .approved(false)
                .build();
        
        return feedbackRepository.save(feedback);
    }
    
    // READ - Get approved feedbacks
    public List<Feedback> getApprovedFeedbacks() {
        return feedbackRepository.findByApprovedTrue();
    }
    
    // READ - Get all feedbacks
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }
    
    // UPDATE - Approve feedback
    @Transactional
    public void approveFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedback.setApproved(true);
        feedbackRepository.save(feedback);
    }
    
    // UPDATE - Disapprove feedback
    @Transactional
    public void disapproveFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedback.setApproved(false);
        feedbackRepository.save(feedback);
    }
}

