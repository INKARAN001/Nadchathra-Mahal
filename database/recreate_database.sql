-- ==========================================
-- DEVELOPER: JADAVAN
-- CRUD: Database Recreation Script
-- ==========================================
-- Complete database recreation script for Hall Booking System
-- This script drops and recreates the entire database with all tables

-- Drop database if exists (CAUTION: This will delete all data!)
DROP DATABASE IF EXISTS hall_booking_db;

-- Create database
CREATE DATABASE hall_booking_db;
USE hall_booking_db;

-- ==========================================
-- DEVELOPER: JADAVAN
-- CRUD: Core User Management Tables
-- ==========================================

-- Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- DEVELOPER: INKARAN
-- CRUD: Admin Management Tables
-- ==========================================

-- Admin Table
CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    user_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    total_bookings INT NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_user_status (user_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Login Info Table
CREATE TABLE login_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    login_date DATE NOT NULL,
    login_time TIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_login_date (login_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Feedback Table
CREATE TABLE feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    approved BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_approved (approved),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Messages Table
CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    recipient_id BIGINT NULL,
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    message_type VARCHAR(20) NOT NULL,
    is_read BOOLEAN NULL, -- NULL for broadcast messages, true/false for direct messages
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_sender (sender_id),
    INDEX idx_recipient (recipient_id),
    INDEX idx_message_type (message_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- DEVELOPER: SRIKARSAN
-- CRUD: Manager Management Tables
-- ==========================================

-- Halls Table
CREATE TABLE halls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    capacity INT NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_name (name),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Food Items Table
CREATE TABLE food_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    price_per_person DOUBLE NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_name (name),
    INDEX idx_category (category),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- DEVELOPER: AKASH
-- CRUD: Booking & Payment Management Tables
-- ==========================================

-- Bookings Table (Basic Information)
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    hall VARCHAR(50) NOT NULL,
    booking_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_booking_date (booking_date),
    INDEX idx_hall (hall)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Booking Details Table (Additional Information)
CREATE TABLE booking_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE,
    food_type VARCHAR(100) NOT NULL,
    people_count INT NOT NULL,
    payment_type VARCHAR(20) NOT NULL,
    total_amount DOUBLE NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    booking_status VARCHAR(20) NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    INDEX idx_booking_id (booking_id),
    INDEX idx_booking_status (booking_status),
    INDEX idx_payment_status (payment_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Payment functionality is handled within booking_details table
-- No separate payment tables needed

-- ==========================================
-- DEVELOPER: JADAVAN
-- CRUD: Default Data Insertion
-- ==========================================

-- Insert Default Admin User
-- Password: admin123 (BCrypt hashed)
INSERT INTO users (name, email, password, phone, role, enabled) 
VALUES ('Admin', 'admin@gsc.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '1234567890', 'ADMIN', TRUE);

-- Insert Default Manager User
-- Password: manager123 (BCrypt hashed)
INSERT INTO users (name, email, password, phone, role, enabled) 
VALUES ('Manager', 'manager@gsc.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '9876543210', 'MANAGER', TRUE);

-- Create Admin Record for Default Admin
INSERT INTO admin (user_id, user_status, total_bookings) 
VALUES (1, 'ACTIVE', 0);

-- ==========================================
-- DEVELOPER: SRIKARSAN
-- CRUD: Default Halls and Food Items
-- ==========================================

-- Insert Default Halls
INSERT INTO halls (name, capacity, description, active) VALUES
('Hall 1', 800, 'Perfect for medium-sized events', TRUE),
('Hall 2', 800, 'Ideal for large gatherings', TRUE),
('Garden Venue (Outdoor)', 800, 'Beautiful outdoor setting with natural ambiance', TRUE);

-- Insert Default Food Items
INSERT INTO food_items (name, price_per_person, description, category, active) VALUES
('Green Delight', 500.0, 'Fresh vegetarian menu with local specialties', 'VEG', TRUE),
('Royal Feast', 800.0, 'Premium non-vegetarian buffet with exotic dishes', 'NON_VEG', TRUE),
('Spicy Grill', 750.0, 'Chicken specialties with spicy marinades', 'NON_VEG', TRUE),
('Ocean Treasure', 1000.0, 'Fresh seafood deluxe with premium fish and prawns', 'SEAFOOD', TRUE);

-- ==========================================
-- DEVELOPER: INKARAN
-- CRUD: Sample Data for Testing
-- ==========================================

-- Sample User for Testing
INSERT INTO users (name, email, password, phone, role, enabled) 
VALUES ('John Doe', 'john@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '9876543210', 'USER', TRUE);

-- Sample Booking for Testing
INSERT INTO bookings (user_id, full_name, phone_number, email_address, hall, booking_date)
VALUES (3, 'John Doe', '9876543210', 'john@example.com', 'Hall 1', '2025-12-15');

-- Sample Booking Details
INSERT INTO booking_details (booking_id, food_type, people_count, payment_type, total_amount, payment_status, booking_status)
VALUES (1, 'Royal Feast', 150, 'CARD', 120000.00, 'PENDING', 'PENDING');

-- Payment information is stored in booking_details table

-- Sample Feedback
INSERT INTO feedback (user_id, message, approved, created_at)
VALUES (3, 'Excellent service and beautiful venue! Highly recommended.', TRUE, NOW());

-- Sample Direct Message
INSERT INTO messages (sender_id, recipient_id, subject, content, message_type, is_read, created_at)
VALUES (1, 3, 'Welcome to GSC Hall Booking', 'Thank you for choosing our hall booking service. We look forward to serving you!', 'DIRECT', FALSE, NOW());

-- Sample Broadcast Message (is_read is NULL)
INSERT INTO messages (sender_id, recipient_id, subject, content, message_type, is_read, created_at)
VALUES (1, NULL, 'System Maintenance Notice', 'The system will be under maintenance on Sunday from 2 AM to 4 AM. We apologize for any inconvenience.', 'BROADCAST', NULL, NOW());

-- Sample Login Info
INSERT INTO login_info (user_id, login_date, login_time)
VALUES (1, CURDATE(), CURTIME()),
       (2, CURDATE(), CURTIME()),
       (3, CURDATE(), CURTIME());

-- Display all tables
SHOW TABLES;

-- Display table information
SELECT 
    TABLE_NAME as 'Table Name',
    TABLE_ROWS as 'Rows',
    CREATE_TIME as 'Created'
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'hall_booking_db'
ORDER BY TABLE_NAME;

-- Display sample data
SELECT 'Users' as Table_Name, COUNT(*) as Count FROM users
UNION ALL
SELECT 'Admins', COUNT(*) FROM admin
UNION ALL
SELECT 'Halls', COUNT(*) FROM halls
UNION ALL
SELECT 'Food Items', COUNT(*) FROM food_items
UNION ALL
SELECT 'Bookings', COUNT(*) FROM bookings
UNION ALL
SELECT 'Booking Details', COUNT(*) FROM booking_details
UNION ALL
SELECT 'Feedback', COUNT(*) FROM feedback
UNION ALL
SELECT 'Messages', COUNT(*) FROM messages
UNION ALL
SELECT 'Login Info', COUNT(*) FROM login_info;

-- Success message
SELECT 'Database recreation completed successfully!' as Status;
