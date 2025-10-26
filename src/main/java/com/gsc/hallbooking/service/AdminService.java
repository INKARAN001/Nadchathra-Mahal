package com.gsc.hallbooking.service;

import com.gsc.hallbooking.entity.Admin;
import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.repository.AdminRepository;
import com.gsc.hallbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * DEVELOPER: INKARAN
 * CRUD: Admin Service - Admin Management
 */
@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    
    // CREATE - Create new admin record
    @Transactional
    public Admin createAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if admin record already exists
        if (adminRepository.existsByUserId(userId)) {
            throw new RuntimeException("Admin record already exists for this user");
        }
        
        Admin admin = Admin.builder()
                .user(user)
                .userStatus("ACTIVE")
                .totalBookings(0)
                .build();
        
        return adminRepository.save(admin);
    }
    
    // CREATE - Create admin with specific values
    @Transactional
    public Admin createAdmin(Long userId, String userStatus, Integer totalBookings) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if admin record already exists
        if (adminRepository.existsByUserId(userId)) {
            throw new RuntimeException("Admin record already exists for this user");
        }
        
        Admin admin = Admin.builder()
                .user(user)
                .userStatus(userStatus != null ? userStatus : "ACTIVE")
                .totalBookings(totalBookings != null ? totalBookings : 0)
                .build();
        
        return adminRepository.save(admin);
    }
    
    // UPDATE - Update admin status
    @Transactional
    public Admin updateAdminStatus(Long adminId, String status) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        admin.setUserStatus(status);
        return adminRepository.save(admin);
    }
    
    /**
     * Increment total bookings count
     */
    @Transactional
    public Admin incrementBookingCount(Long userId) {
        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin record not found for user"));
        
        admin.setTotalBookings(admin.getTotalBookings() + 1);
        return adminRepository.save(admin);
    }
    
    /**
     * Update total bookings count
     */
    @Transactional
    public Admin updateBookingCount(Long adminId, Integer totalBookings) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        admin.setTotalBookings(totalBookings);
        return adminRepository.save(admin);
    }
    
    // READ - Get admin by user ID
    public Optional<Admin> getAdminByUserId(Long userId) {
        return adminRepository.findByUserId(userId);
    }
    
    // READ - Get admin by ID
    public Optional<Admin> getAdminById(Long adminId) {
        return adminRepository.findById(adminId);
    }
    
    // READ - Get all admins
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    /**
     * Get admins by status (ACTIVE or DISABLE)
     */
    public List<Admin> getAdminsByStatus(String status) {
        return adminRepository.findByUserStatus(status);
    }
    
    /**
     * Get all admins ordered by total bookings (descending)
     */
    public List<Admin> getAdminsOrderedByBookings() {
        return adminRepository.findAllByOrderByTotalBookingsDesc();
    }
    
    // DELETE - Delete admin record
    @Transactional
    public void deleteAdmin(Long adminId) {
        adminRepository.deleteById(adminId);
    }
    
    /**
     * Check if admin record exists for user
     */
    public boolean adminExistsForUser(Long userId) {
        return adminRepository.existsByUserId(userId);
    }
}

