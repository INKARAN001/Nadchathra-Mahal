@echo off
echo ========================================
echo   Nadchathra Mahal Payment System
echo ========================================
echo.
echo Starting application with Maven Spring Boot plugin...
echo This will download dependencies and start the server.
echo.
echo Press Ctrl+C to stop the application
echo.
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=payment
