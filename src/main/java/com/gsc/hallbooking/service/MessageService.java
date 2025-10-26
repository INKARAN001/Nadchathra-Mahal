package com.gsc.hallbooking.service;

import com.gsc.hallbooking.entity.Message;
import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.repository.MessageRepository;
import com.gsc.hallbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DEVELOPER: INKARAN
 * CRUD: Message Management System
 */
@Service
@RequiredArgsConstructor
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    
    // CREATE - Send broadcast message
    @Transactional
    public Message sendBroadcastMessage(User admin, String subject, String content) {
        Message message = Message.builder()
                .sender(admin)
                .recipient(null) // null for broadcast messages
                .subject(subject)
                .content(content)
                .messageType("BROADCAST")
                .isRead(null) // NULL for broadcast messages (no read status)
                .build();
        
        return messageRepository.save(message);
    }
    
    // CREATE - Send direct message
    @Transactional
    public Message sendDirectMessage(User admin, Long recipientId, String subject, String content) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient user not found"));
        
        Message message = Message.builder()
                .sender(admin)
                .recipient(recipient)
                .subject(subject)
                .content(content)
                .messageType("DIRECT")
                .isRead(false)
                .build();
        
        return messageRepository.save(message);
    }
    
    // READ - Get messages for user
    public List<Message> getMessagesForUser(User user) {
        return messageRepository.findByRecipientOrBroadcast(user);
    }
    
    // READ - Get unread messages for user
    public List<Message> getUnreadMessagesForUser(User user) {
        return messageRepository.findUnreadMessagesForUser(user);
    }
    
    // READ - Get broadcast messages for dashboard alerts
    public List<Message> getBroadcastMessages() {
        return messageRepository.findByMessageTypeAndRecipientIsNullOrderByCreatedAtDesc("BROADCAST");
    }
    
    // READ - Get unread message count for user
    public Long getUnreadMessageCountForUser(User user) {
        return messageRepository.countUnreadMessagesForUser(user);
    }
    
    // UPDATE - Mark message as read (only for direct messages)
    @Transactional
    public void markMessageAsRead(Long messageId, User user) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        // Only allow marking direct messages as read
        if (message.getRecipient() != null && message.getRecipient().equals(user) && 
            "DIRECT".equals(message.getMessageType())) {
            message.setIsRead(true);
            messageRepository.save(message);
        } else if ("BROADCAST".equals(message.getMessageType())) {
            throw new RuntimeException("Cannot mark broadcast messages as read");
        } else {
            throw new RuntimeException("Unauthorized to mark this message as read");
        }
    }
    
    // UPDATE - Mark all direct messages as read (broadcast messages are not affected)
    @Transactional
    public void markAllMessagesAsRead(User user) {
        List<Message> unreadMessages = getUnreadMessagesForUser(user);
        for (Message message : unreadMessages) {
            // Only mark direct messages as read, skip broadcast messages
            if ("DIRECT".equals(message.getMessageType())) {
                message.setIsRead(true);
            }
        }
        messageRepository.saveAll(unreadMessages);
    }
    
    // READ - Get all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    
    // READ - Get messages by sender
    public List<Message> getMessagesBySender(User sender) {
        return messageRepository.findBySenderOrderByCreatedAtDesc(sender);
    }
    
    // READ - Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // READ - Get message by ID
    public Message getMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }
    
    // UPDATE - Update message
    @Transactional
    public Message updateMessage(Long messageId, String subject, String content) {
        Message message = getMessageById(messageId);
        message.setSubject(subject);
        message.setContent(content);
        return messageRepository.save(message);
    }
    
    // DELETE - Delete message
    @Transactional
    public void deleteMessage(Long messageId) {
        Message message = getMessageById(messageId);
        messageRepository.delete(message);
    }
    
    // READ - Get messages by type
    public List<Message> getMessagesByType(String messageType) {
        if ("BROADCAST".equals(messageType)) {
            return messageRepository.findByMessageTypeAndRecipientIsNullOrderByCreatedAtDesc(messageType);
        } else {
            return messageRepository.findByMessageTypeOrderByCreatedAtDesc(messageType);
        }
    }
}
