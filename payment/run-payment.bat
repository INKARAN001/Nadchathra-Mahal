@echo off
echo Starting Payment Application...
echo.
echo Using Maven Spring Boot plugin to run the application...
echo.
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=payment
pause
