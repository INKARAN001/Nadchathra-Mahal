-- Migration: Add login_info table
-- Date: 2025-10-15
-- Description: Creates the login_info table to track user login history

USE hall_booking_db;

-- Create login_info table
CREATE TABLE IF NOT EXISTS login_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    login_date DATE NOT NULL,
    login_time TIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_login_date (login_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Verify table creation
SHOW CREATE TABLE login_info;

