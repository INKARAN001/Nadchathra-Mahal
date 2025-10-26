package com.gsc.hallbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DEVELOPER: INKARAN
 * CRUD: Message Entity - Message Management
 */
@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // Admin who sent the message
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = true)
    private User recipient; // Specific user (null for broadcast messages)
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private String messageType; // BROADCAST or DIRECT
    
    @Column(nullable = false)
    private String subject;
    
    @Column(name = "is_read", nullable = true)
    private Boolean isRead; // NULL for broadcast messages, true/false for direct messages
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        // For broadcast messages, isRead should remain NULL
        // For direct messages, set to false if not already set
        if (isRead == null && "DIRECT".equals(messageType)) {
            isRead = false;
        }
    }
}
