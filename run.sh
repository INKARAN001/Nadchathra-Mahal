#!/bin/bash

echo "===================================="
echo "GSC Hall Booking System"
echo "===================================="
echo ""

echo "Checking Java version..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    exit 1
fi
java -version
echo ""

echo "Checking Maven..."
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    exit 1
fi
mvn -version
echo ""

echo "Building the project..."
mvn clean install
if [ $? -ne 0 ]; then
    echo "ERROR: Build failed"
    exit 1
fi
echo ""

echo "Starting the application..."
echo ""
echo "The application will be available at: http://localhost:8081"
echo ""
echo "Default Admin Credentials:"
echo "Email: admin@gsc.com"
echo "Password: admin123"
echo ""
echo "Press Ctrl+C to stop the application"
echo ""

mvn spring-boot:run

