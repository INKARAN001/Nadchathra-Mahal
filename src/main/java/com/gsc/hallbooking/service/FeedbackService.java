package com.gsc.hallbooking.service;

import com.gsc.hallbooking.entity.Feedback;
import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    
    private final FeedbackRepository feedbackRepository;
    
    @Transactional
    public Feedback createFeedback(User user, String message) {
        Feedback feedback = Feedback.builder()
                .user(user)
                .message(message)
                .approved(false)
                .build();
        
        return feedbackRepository.save(feedback);
    }
    
    public List<Feedback> getApprovedFeedbacks() {
        return feedbackRepository.findByApprovedTrue();
    }
    
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }
    
    @Transactional
    public void approveFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedback.setApproved(true);
        feedbackRepository.save(feedback);
    }
    
    @Transactional
    public void disapproveFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedback.setApproved(false);
        feedbackRepository.save(feedback);
    }
}

