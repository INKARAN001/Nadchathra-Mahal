-- Migration: Add admin table
-- Date: 2025-10-15
-- Description: Creates the admin table to track admin user information and statistics

USE hall_booking_db;

-- Create admin table
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    user_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    total_bookings INT NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_user_status (user_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Verify table creation
SHOW CREATE TABLE admin;

-- Optional: Create admin records for existing admin users
-- INSERT INTO admin (user_id, user_status, total_bookings)
-- SELECT id, 'ACTIVE', 0 FROM users WHERE role = 'ADMIN'
-- ON DUPLICATE KEY UPDATE user_id = user_id;

