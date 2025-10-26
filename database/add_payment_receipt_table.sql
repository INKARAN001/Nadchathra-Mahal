-- Migration: Add payment_receipt table
-- Date: 2025-10-15
-- Description: Creates the payment_receipt table to store receipt information for payments

USE hall_booking_db;

-- Create payment_receipt table
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

-- Verify table creation
SHOW CREATE TABLE payment_receipt;

