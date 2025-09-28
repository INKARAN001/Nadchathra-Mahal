@echo off
echo Testing Payment Application Startup...
echo.

echo 1. Checking if target directory exists...
if not exist "target" (
    echo ERROR: target directory not found. Please run 'mvn clean compile' first.
    pause
    exit /b 1
)

echo 2. Checking if classes exist...
if not exist "target\classes\com\example\payment\PaymentApplication.class" (
    echo ERROR: PaymentApplication.class not found. Please run 'mvn clean compile' first.
    pause
    exit /b 1
)

echo 3. Checking if lib directory exists...
if not exist "target\lib" (
    echo ERROR: target\lib directory not found. Please run 'mvn dependency:copy-dependencies' first.
    pause
    exit /b 1
)

echo 4. Starting application...
echo.
java -cp "target/classes;target/lib/*" com.example.payment.PaymentApplication

pause
