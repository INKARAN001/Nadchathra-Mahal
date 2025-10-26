package com.gsc.hallbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DEVELOPER: INKARAN
 * CRUD: LoginInfo Entity - Login Tracking
 */
@Entity
@Table(name = "login_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "login_date", nullable = false)
    private LocalDate loginDate;
    
    @Column(name = "login_time", nullable = false)
    private LocalTime loginTime;
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (loginDate == null) {
            loginDate = now.toLocalDate();
        }
        if (loginTime == null) {
            loginTime = now.toLocalTime();
        }
    }
}

