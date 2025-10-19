-- Fix script for existing database
-- Run this if you're having login issues

USE hall_booking_db;

-- Add enabled column if it doesn't exist
ALTER TABLE users 
ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;

-- Update existing users to have enabled = true
UPDATE users SET enabled = TRUE WHERE enabled IS NULL;

-- Verify admin user exists and is enabled
UPDATE users SET enabled = TRUE WHERE email = 'admin@gsc.com';

-- Check the results
SELECT id, name, email, role, enabled FROM users;

