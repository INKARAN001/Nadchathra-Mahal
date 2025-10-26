@echo off
REM ==========================================
REM DEVELOPER: JADAVAN
REM CRUD: Database Recreation Script for Windows
REM ==========================================

echo ====================================
echo Hall Booking System Database Setup
echo ====================================
echo.

REM Check if MySQL is available
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: MySQL is not installed or not in PATH
    echo Please install MySQL and add it to your PATH
    pause
    exit /b 1
)

echo MySQL found. Proceeding with database recreation...
echo.

REM Prompt for MySQL root password
set /p mysql_password="Enter MySQL root password: "

echo.
echo WARNING: This will delete all existing data in hall_booking_db!
set /p confirm="Are you sure you want to continue? (y/N): "
if /i not "%confirm%"=="y" (
    echo Operation cancelled.
    pause
    exit /b 0
)

echo.
echo Creating database...
mysql -u root -p%mysql_password% < database\recreate_database.sql

if %errorlevel% equ 0 (
    echo.
    echo ====================================
    echo Database recreation completed successfully!
    echo ====================================
    echo.
    echo Default login credentials:
    echo Admin: admin@gsc.com / admin123
    echo Manager: manager@gsc.com / manager123
    echo.
    echo You can now start the Spring Boot application.
) else (
    echo.
    echo ERROR: Database recreation failed!
    echo Please check the MySQL error messages above.
)

echo.
pause
