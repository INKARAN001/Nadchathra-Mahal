package com.gsc.hallbooking.service;

import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.repository.AdminRepository;
import com.gsc.hallbooking.repository.LoginInfoRepository;
import com.gsc.hallbooking.repository.MessageRepository;
import com.gsc.hallbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * DEVELOPER: JADAVAN
 * CRUD: User Management & Profile Operations
 */
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final LoginInfoRepository loginInfoRepository;
    private final MessageRepository messageRepository;
    
    @Transactional
    public User registerUser(String name, String email, String password, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .phone(phone)
                .role("USER")
                .enabled(true)
                .build();
        
        return userRepository.save(user);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Transactional
    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if ("ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Cannot disable admin accounts");
        }
        
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
        
        // Update admin table status to match user enabled status
        adminRepository.findByUserId(userId).ifPresent(admin -> {
            admin.setUserStatus(user.getEnabled() ? "ACTIVE" : "DISABLE");
            adminRepository.save(admin);
        });
    }
    
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if ("ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Cannot delete admin accounts");
        }
        
        // Delete all related records first to avoid foreign key constraint violations
        
        // Delete admin record if exists
        adminRepository.findByUserId(userId).ifPresent(adminRepository::delete);
        
        // Delete login info records
        loginInfoRepository.findByUserId(userId).forEach(loginInfoRepository::delete);
        
        // Delete messages where user is sender or recipient
        messageRepository.findBySenderOrderByCreatedAtDesc(user).forEach(messageRepository::delete);
        messageRepository.findByRecipientOrderByCreatedAtDesc(user).forEach(messageRepository::delete);
        
        // Delete the user (this will cascade delete bookings and feedbacks due to CascadeType.ALL)
        userRepository.delete(user);
    }
    
    // DEVELOPER: JADAVAN - User Profile CRUD Operations
    
    // UPDATE - Update user profile information
    @Transactional
    public User updateUserProfile(Long userId, String name, String phone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setName(name);
        user.setPhone(phone);
        
        return userRepository.save(user);
    }
    
    // UPDATE - Update user password
    @Transactional
    public void updateUserPassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        // Update to new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    // DELETE - Delete user account
    @Transactional
    public void deleteOwnAccount(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verify password before deletion
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        
        if ("ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Cannot delete admin accounts");
        }
        
        // Delete all related records first to avoid foreign key constraint violations
        
        // Delete admin record if exists
        adminRepository.findByUserId(userId).ifPresent(adminRepository::delete);
        
        // Delete login info records
        loginInfoRepository.findByUserId(userId).forEach(loginInfoRepository::delete);
        
        // Delete messages where user is sender or recipient
        messageRepository.findBySenderOrderByCreatedAtDesc(user).forEach(messageRepository::delete);
        messageRepository.findByRecipientOrderByCreatedAtDesc(user).forEach(messageRepository::delete);
        
        // Delete the user (this will cascade delete bookings and feedbacks due to CascadeType.ALL)
        userRepository.delete(user);
    }
    
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

