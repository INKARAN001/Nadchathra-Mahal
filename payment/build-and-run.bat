@echo off
echo Building and Running Payment Application...
echo.

echo 1. Cleaning previous build...
call mvn clean

echo.
echo 2. Compiling application...
call mvn compile

echo.
echo 3. Copying dependencies...
call mvn dependency:copy-dependencies

echo.
echo 4. Starting application...
echo.
java -cp "target/classes;target/lib/*" com.example.payment.PaymentApplication

pause
