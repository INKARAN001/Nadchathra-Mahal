package com.example.payment.service;

import com.example.payment.dto.UserRequirementDTO;
import com.example.payment.entity.UserRequirement;
import com.example.payment.respository.UserRequirementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserRequirementService {

    private final UserRequirementRepository repository;

    public UserRequirementService(UserRequirementRepository repository) {
        this.repository = repository;
    }

    public UserRequirement saveUserRequirement(UserRequirementDTO dto) {
        UserRequirement userRequirement = new UserRequirement();
        
        // Map DTO to Entity
        userRequirement.setFullName(dto.getFullName());
        userRequirement.setEmail(dto.getEmail());
        userRequirement.setPhone(dto.getPhone());
        userRequirement.setDateOfBirth(dto.getDateOfBirth()); // Can be null
        userRequirement.setEventType(dto.getEventType());
        userRequirement.setEventDate(dto.getEventDate());
        userRequirement.setGuestCount(dto.getGuestCount());
        userRequirement.setBudget(dto.getBudget());
        userRequirement.setSpecialRequirements(dto.getSpecialRequirements());
        userRequirement.setCuisineType(dto.getCuisineType());
        userRequirement.setMealType(dto.getMealType());
        userRequirement.setDietaryRestrictions(dto.getDietaryRestrictions());
        userRequirement.setBeveragePreferences(dto.getBeveragePreferences());
        userRequirement.setFoodNotes(dto.getFoodNotes());
        userRequirement.setCardNumber(dto.getCardNumber());
        userRequirement.setExpiryMonth(dto.getExpiryMonth());
        userRequirement.setExpiryYear(dto.getExpiryYear());
        userRequirement.setCvv(dto.getCvv());
        userRequirement.setCardHolderName(dto.getCardHolderName());
        userRequirement.setPaymentStatus("PENDING");
        userRequirement.setAmount(50000.00); // LKR 50,000
        
        // Set user ID - this is critical for user-specific filtering
        userRequirement.setUserId(dto.getUserId());
        
        return repository.save(userRequirement);
    }

    public List<UserRequirement> getAllUserRequirements() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public List<UserRequirement> getUserRequirementsByUserId(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<UserRequirement> getUserRequirementById(Long id) {
        return repository.findById(id);
    }

    public List<UserRequirement> getUserRequirementsByEmail(String email) {
        return repository.findByEmailOrderByCreatedAtDesc(email);
    }

    public List<UserRequirement> getUserRequirementsByEventType(String eventType) {
        return repository.findByEventTypeOrderByCreatedAtDesc(eventType);
    }

    public List<UserRequirement> getUserRequirementsByPaymentStatus(String paymentStatus) {
        return repository.findByPaymentStatusOrderByCreatedAtDesc(paymentStatus);
    }

    public UserRequirement updatePaymentStatus(Long id, String paymentStatus, Long transactionId) {
        Optional<UserRequirement> optional = repository.findById(id);
        if (optional.isPresent()) {
            UserRequirement userRequirement = optional.get();
            userRequirement.setPaymentStatus(paymentStatus);
            userRequirement.setTransactionId(transactionId);
            return repository.save(userRequirement);
        }
        return null;
    }

    public long getTotalUserRequirements() {
        return repository.count();
    }

    public long getCountByEventType(String eventType) {
        return repository.countByEventType(eventType);
    }

    public long getCountByPaymentStatus(String paymentStatus) {
        return repository.countByPaymentStatus(paymentStatus);
    }

    public void deleteUserRequirement(Long id) {
        repository.deleteById(id);
    }

    public UserRequirement updateApprovalStatus(Long id, String approvalStatus, String rejectionReason) {
        Optional<UserRequirement> optional = repository.findById(id);
        if (optional.isPresent()) {
            UserRequirement userRequirement = optional.get();
            userRequirement.setApprovalStatus(approvalStatus);
            userRequirement.setApprovedAt(java.time.LocalDateTime.now());
            // Set approved_by to admin user ID (1 for admin)
            userRequirement.setApprovedBy(1L);
            if (rejectionReason != null) {
                userRequirement.setRejectionReason(rejectionReason);
            }
            return repository.save(userRequirement);
        }
        return null;
    }
}
