package com.gsc.hallbooking.repository;

import com.gsc.hallbooking.entity.Message;
import com.gsc.hallbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    // Find all messages for a specific user (both direct and broadcast)
    @Query("SELECT m FROM Message m WHERE m.recipient = :user OR (m.recipient IS NULL AND m.messageType = 'BROADCAST') ORDER BY m.createdAt DESC")
    List<Message> findByRecipientOrBroadcast(User user);
    
    // Find all broadcast messages
    List<Message> findByMessageTypeAndRecipientIsNullOrderByCreatedAtDesc(String messageType);
    
    // Find all direct messages for a specific user
    List<Message> findByRecipientOrderByCreatedAtDesc(User recipient);
    
    // Find unread direct messages for a specific user (broadcast messages are not included)
    @Query("SELECT m FROM Message m WHERE m.recipient = :user AND m.messageType = 'DIRECT' AND m.isRead = false ORDER BY m.createdAt DESC")
    List<Message> findUnreadMessagesForUser(User user);
    
    // Count unread direct messages for a specific user (broadcast messages are not included)
    @Query("SELECT COUNT(m) FROM Message m WHERE m.recipient = :user AND m.messageType = 'DIRECT' AND m.isRead = false")
    Long countUnreadMessagesForUser(User user);
    
    // Find all messages sent by a specific admin
    List<Message> findBySenderOrderByCreatedAtDesc(User sender);
    
    // Find messages by message type
    List<Message> findByMessageTypeOrderByCreatedAtDesc(String messageType);
}
