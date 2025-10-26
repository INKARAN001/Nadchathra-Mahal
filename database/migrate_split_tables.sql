-- Migration Script: Split Bookings Table
-- This script migrates data from old bookings table to new split table structure
-- Run this AFTER creating the new schema with schema.sql

USE hall_booking_db;

-- Step 1: Backup existing bookings table (optional but recommended)
CREATE TABLE IF NOT EXISTS bookings_backup AS SELECT * FROM bookings;

-- Step 2: Create temporary table with old structure to hold existing data
CREATE TABLE IF NOT EXISTS bookings_old AS SELECT * FROM bookings;

-- Step 3: Drop the old bookings table
DROP TABLE IF EXISTS bookings;

-- Step 4: Create new bookings table structure
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

-- Step 5: Create booking_details table
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

-- Step 6: Migrate data from old table to new bookings table
INSERT INTO bookings (id, user_id, full_name, phone_number, email_address, hall, booking_date)
SELECT id, user_id, full_name, phone_number, email_address, hall, booking_date
FROM bookings_old;

-- Step 7: Migrate data to booking_details table
INSERT INTO booking_details (booking_id, food_type, people_count, payment_type, total_amount, payment_status, booking_status)
SELECT id, food_type, people_count, payment_type, total_amount, payment_status, booking_status
FROM bookings_old;

-- Step 8: Drop temporary table
DROP TABLE IF EXISTS bookings_old;

-- Step 9: Verify migration
SELECT 
    (SELECT COUNT(*) FROM bookings) as bookings_count,
    (SELECT COUNT(*) FROM booking_details) as booking_details_count,
    (SELECT COUNT(*) FROM bookings_backup) as backup_count;

-- Note: Keep bookings_backup table for safety until you verify everything works correctly
-- You can drop it later with: DROP TABLE IF EXISTS bookings_backup;

SELECT 'Migration completed successfully!' as status;

