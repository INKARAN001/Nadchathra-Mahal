-- Hall Booking System Database Schema
-- Create Database
CREATE DATABASE IF NOT EXISTS hall_booking_db;
USE hall_booking_db;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
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

-- Admin Table
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    user_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    total_bookings INT NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_user_status (user_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- RPayment Table
CREATE TABLE IF NOT EXISTS rpayment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    payment_date DATETIME NOT NULL,
    amount DOUBLE NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bookings Table (Basic Information)
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    hall VARCHAR(50) NOT NULL,
    booking_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_booking_date (booking_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Booking Details Table (Additional Information)
CREATE TABLE IF NOT EXISTS booking_details (
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

-- Feedback Table
CREATE TABLE IF NOT EXISTS feedback (
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

-- Payment Table
CREATE TABLE IF NOT EXISTS payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    payment_date DATETIME NOT NULL,
    amount DOUBLE NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    INDEX idx_booking_id (booking_id),
    INDEX idx_status (status),
    INDEX idx_payment_date (payment_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Payment Receipt Table
CREATE TABLE IF NOT EXISTS payment_receipt (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_id BIGINT NOT NULL,
    receipt_number VARCHAR(100) NOT NULL UNIQUE,
    amount DOUBLE NOT NULL,
    generated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_id) REFERENCES payment(id) ON DELETE CASCADE,
    INDEX idx_payment_id (payment_id),
    INDEX idx_receipt_number (receipt_number),
    INDEX idx_generated_at (generated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Login Info Table
CREATE TABLE IF NOT EXISTS login_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    login_date DATE NOT NULL,
    login_time TIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_login_date (login_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Messages Table
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    recipient_id BIGINT NULL,
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    message_type VARCHAR(20) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_sender (sender_id),
    INDEX idx_recipient (recipient_id),
    INDEX idx_message_type (message_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert Default Admin User
-- Password: admin123 (BCrypt hashed)
INSERT INTO users (name, email, password, phone, role) 
VALUES ('Admin', 'admin@gsc.com', '$2a$10$YourBCryptHashedPasswordHere', '1234567890', 'ADMIN')
ON DUPLICATE KEY UPDATE email = email;

-- Sample Data for Testing (Optional)

-- Sample User
-- INSERT INTO users (name, email, password, phone, role) 
-- VALUES ('John Doe', 'john@example.com', '$2a$10$YourBCryptHashedPasswordHere', '9876543210', 'USER');

-- Sample Booking (Note: Now split into two tables)
-- First insert into bookings table
-- INSERT INTO bookings (user_id, full_name, phone_number, email_address, hall, booking_date)
-- VALUES (2, 'John Doe', '9876543210', 'john@example.com', 'Hall A', '2025-11-15');

-- Then insert into booking_details table (assuming booking id is 1)
-- INSERT INTO booking_details (booking_id, food_type, people_count, payment_type, total_amount, payment_status, booking_status)
-- VALUES (1, 'Royal Feast', 150, 'CARD', 120000.00, 'PENDING', 'PENDING');

-- Sample Feedback
-- INSERT INTO feedback (user_id, message, approved, created_at)
-- VALUES (2, 'Excellent service and beautiful venue! Highly recommended.', TRUE, NOW());

-- Display all tables
SHOW TABLES;

