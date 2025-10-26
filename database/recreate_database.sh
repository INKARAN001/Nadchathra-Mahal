#!/bin/bash
# ==========================================
# DEVELOPER: JADAVAN
# CRUD: Database Recreation Script for Linux/Mac
# ==========================================

echo "===================================="
echo "Hall Booking System Database Setup"
echo "===================================="
echo

# Check if MySQL is available
if ! command -v mysql &> /dev/null; then
    echo "ERROR: MySQL is not installed or not in PATH"
    echo "Please install MySQL and add it to your PATH"
    exit 1
fi

echo "MySQL found. Proceeding with database recreation..."
echo

# Prompt for MySQL root password
read -s -p "Enter MySQL root password: " mysql_password
echo

echo
echo "WARNING: This will delete all existing data in hall_booking_db!"
read -p "Are you sure you want to continue? (y/N): " confirm

if [[ $confirm != [yY] ]]; then
    echo "Operation cancelled."
    exit 0
fi

echo
echo "Creating database..."
mysql -u root -p$mysql_password < database/recreate_database.sql

if [ $? -eq 0 ]; then
    echo
    echo "===================================="
    echo "Database recreation completed successfully!"
    echo "===================================="
    echo
    echo "Default login credentials:"
    echo "Admin: admin@gsc.com / admin123"
    echo "Manager: manager@gsc.com / manager123"
    echo
    echo "You can now start the Spring Boot application."
else
    echo
    echo "ERROR: Database recreation failed!"
    echo "Please check the MySQL error messages above."
    exit 1
fi

echo
