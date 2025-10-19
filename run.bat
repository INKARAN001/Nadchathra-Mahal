@echo off
echo ====================================
echo GSC Hall Booking System
echo ====================================
echo.

echo Checking Java version...
java -version
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    pause
    exit /b 1
)
echo.

echo Checking Maven...
mvn -version
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    pause
    exit /b 1
)
echo.

echo Building the project...
call mvn clean install
if errorlevel 1 (
    echo ERROR: Build failed
    pause
    exit /b 1
)
echo.

echo Starting the application...
echo.
echo The application will be available at: http://localhost:8081
echo.
echo Default Admin Credentials:
echo Email: admin@gsc.com
echo Password: admin123
echo.
echo Press Ctrl+C to stop the application
echo.

call mvn spring-boot:run

