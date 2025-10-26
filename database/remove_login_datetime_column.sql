-- Migration: Remove login_datetime column from login_info table
-- Date: 2025-10-15
-- Description: Removes the login_datetime column as it's redundant with login_date and login_time

USE hall_booking_db;

-- Check if the column exists before dropping
SET @dbname = DATABASE();
SET @tablename = "login_info";
SET @columnname = "login_datetime";
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_NAME = @tablename)
      AND (TABLE_SCHEMA = @dbname)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  "ALTER TABLE login_info DROP COLUMN login_datetime, DROP INDEX idx_login_datetime;",
  "SELECT 'Column login_datetime does not exist, skipping...' AS message;"
));

PREPARE alterIfExists FROM @preparedStatement;
EXECUTE alterIfExists;
DEALLOCATE PREPARE alterIfExists;

-- Verify the change
DESCRIBE login_info;

