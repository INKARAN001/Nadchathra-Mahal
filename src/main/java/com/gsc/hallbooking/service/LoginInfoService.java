package com.gsc.hallbooking.service;

import com.gsc.hallbooking.entity.LoginInfo;
import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.repository.LoginInfoRepository;
import com.gsc.hallbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DEVELOPER: INKARAN
 * CRUD: Login Tracking Service
 */
@Service
@RequiredArgsConstructor
public class LoginInfoService {
    
    private final LoginInfoRepository loginInfoRepository;
    private final UserRepository userRepository;
    
    // CREATE - Record login by email
    @Transactional
    public LoginInfo recordLogin(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found: " + email);
        }
        
        return recordLogin(userOptional.get());
    }
    
    // CREATE - Record login by user
    @Transactional
    public LoginInfo recordLogin(User user) {
        LocalDateTime now = LocalDateTime.now();
        
        LoginInfo loginInfo = LoginInfo.builder()
                .user(user)
                .loginDate(now.toLocalDate())
                .loginTime(now.toLocalTime())
                .build();
        
        return loginInfoRepository.save(loginInfo);
    }
    
    // READ - Get user logins
    public List<LoginInfo> getUserLogins(Long userId) {
        return loginInfoRepository.findByUserIdOrderByLoginDateDescLoginTimeDesc(userId);
    }
    
    // READ - Get recent user logins
    public List<LoginInfo> getRecentUserLogins(Long userId) {
        return loginInfoRepository.findTop10ByUserIdOrderByLoginDateDescLoginTimeDesc(userId);
    }
    
    // READ - Get logins by date
    public List<LoginInfo> getLoginsByDate(LocalDate date) {
        return loginInfoRepository.findByLoginDate(date);
    }
    
    // READ - Get all logins
    public List<LoginInfo> getAllLogins() {
        return loginInfoRepository.findAllByOrderByLoginDateDescLoginTimeDesc();
    }
    
    // READ - Get login count for user
    public long getLoginCount(Long userId) {
        return loginInfoRepository.findByUserId(userId).size();
    }
    
    // READ - Get last login for user
    public Optional<LoginInfo> getLastLogin(Long userId) {
        List<LoginInfo> logins = loginInfoRepository.findTop10ByUserIdOrderByLoginDateDescLoginTimeDesc(userId);
        return logins.isEmpty() ? Optional.empty() : Optional.of(logins.get(0));
    }
}

