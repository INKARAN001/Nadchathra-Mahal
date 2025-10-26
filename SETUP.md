# Setup Guide

This guide provides detailed instructions for setting up the Hall Booking System on your local development environment.

## 📋 Table of Contents

- [System Requirements](#system-requirements)
- [Step-by-Step Installation](#step-by-step-installation)
- [IDE Setup](#ide-setup)
- [Troubleshooting](#troubleshooting)

## 💻 System Requirements

### Minimum Requirements
- **OS**: Windows 10/11, macOS 10.14+, or Linux (Ubuntu 20.04+)
- **RAM**: 4 GB
- **Disk Space**: 1 GB free space
- **Java**: JDK 17 or higher
- **Maven**: 3.6.0 or higher
- **MySQL**: 8.0 or higher

### Recommended Requirements
- **RAM**: 8 GB
- **Disk Space**: 5 GB free space
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code

## 🚀 Step-by-Step Installation

### Step 1: Install Java JDK

#### Windows
1. Download JDK 17 from [Oracle](https://www.oracle.com/java/technologies/downloads/#java17) or [Adoptium](https://adoptium.net/)
2. Run the installer
3. Set `JAVA_HOME` environment variable:
   - Add new system variable `JAVA_HOME` = `C:\Program Files\Java\jdk-17`
   - Add to Path: `%JAVA_HOME%\bin`
4. Verify installation:
   ```bash
   java -version
   ```

#### macOS
```bash
# Using Homebrew
brew install openjdk@17

# Set JAVA_HOME
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-17-jdk
sudo update-alternatives --config java
```

### Step 2: Install Maven

#### Windows
1. Download Maven from [Maven Apache](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Set environment variables:
   - `MAVEN_HOME` = `C:\Program Files\Apache\maven`
   - Add to Path: `%MAVEN_HOME%\bin`
4. Verify installation:
   ```bash
   mvn -version
   ```

#### macOS
```bash
brew install maven
```

#### Linux
```bash
sudo apt install maven
```

### Step 3: Install MySQL

#### Windows
1. Download MySQL Installer from [MySQL Downloads](https://dev.mysql.com/downloads/installer/)
2. Run installer and select "Developer Default"
3. Set root password (remember this for later)
4. Configure MySQL service

#### macOS
```bash
brew install mysql
brew services start mysql
```

#### Linux
```bash
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
sudo mysql_secure_installation
```

### Step 4: Create Database

1. Start MySQL:
   ```bash
   # Windows (if installed as service, it starts automatically)
   # macOS/Linux
   sudo systemctl start mysql
   ```

2. Log in to MySQL:
   ```bash
   mysql -u root -p
   ```

3. Run the setup script:
   ```bash
   # From project root
   mysql -u root -p < database/recreate_database.sql
   ```

   Or use the provided batch/shell scripts:
   ```bash
   # Windows
   cd database
   recreate_database.bat
   
   # Linux/Mac
   cd database
   chmod +x recreate_database.sh
   ./recreate_database.sh
   ```

### Step 5: Configure Application

1. Open `src/main/resources/application.properties`

2. Update database credentials:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=YOUR_PASSWORD
   ```

3. Update port if needed:
   ```properties
   server.port=8081
   ```

### Step 6: Build the Project

From the project root directory:

```bash
# Clean and build
mvn clean install

# Skip tests (faster build)
mvn clean install -DskipTests
```

### Step 7: Run the Application

#### Option A: Using run.bat (Windows only)
```bash
run.bat
```

#### Option B: Using Maven
```bash
mvn spring-boot:run
```

#### Option C: Run JAR file
```bash
java -jar target/hall-booking-system-1.0.0.jar
```

### Step 8: Verify Installation

1. Open your browser and go to: `http://localhost:8081`
2. You should see the login page
3. Log in with admin credentials:
   - Email: admin@gsc.com
   - Password: admin123

## 🔧 IDE Setup

### IntelliJ IDEA

1. **Import Project**
   - Open IntelliJ IDEA
   - File → Open → Select project folder
   - Choose "Import project from external model" → Maven
   - Click Next and Finish

2. **Configure JDK**
   - File → Project Structure → Project
   - Set SDK to JDK 17
   - Set Language level to 17

3. **Enable Lombok**
   - File → Settings → Plugins
   - Search for "Lombok" and install
   - Restart IDE
   - File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors
   - Check "Enable annotation processing"

4. **Configure Run Configuration**
   - Run → Edit Configurations
   - Click + → Application
   - Name: HallBookingApplication
   - Main class: `com.gsc.hallbooking.HallBookingApplication`
   - Module: hall-booking-system
   - JRE: 17

### Eclipse

1. **Import Project**
   - File → Import → Maven → Existing Maven Projects
   - Select the project folder
   - Click Finish

2. **Configure JDK**
   - Right-click project → Properties
   - Java Build Path → Libraries
   - Remove JRE System Library
   - Add Library → JRE System Library → JavaSE-17

3. **Install Lombok**
   - Download lombok.jar from [Project Lombok](https://projectlombok.org/downloads)
   - Run: `java -jar lombok.jar`
   - Point to Eclipse installation directory
   - Restart Eclipse

### VS Code

1. **Install Extensions**
   - Java Extension Pack
   - Spring Boot Extension Pack
   - Maven for Java

2. **Open Project**
   - File → Open Folder
   - Select project directory

3. **Configure Settings**
   - Settings (Ctrl+,) → Java Home
   - Set to JDK 17 path

## 🔍 Troubleshooting

### Common Build Errors

**Error: Java version mismatch**
```bash
# Check Java version
java -version

# Should show 17 or higher
```

**Error: Maven not found**
```bash
# Add to PATH
export PATH=$PATH:/path/to/maven/bin
```

**Error: Database connection failed**
```bash
# Check MySQL is running
sudo systemctl status mysql  # Linux
brew services list          # macOS

# Test connection
mysql -u root -p
```

### Port Already in Use

```bash
# Find process using port 8081
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8081
kill -9 <PID>

# Or change port in application.properties
server.port=8082
```

### Database Migration Issues

If you have schema issues:

```bash
cd database
mysql -u root -p < fix_database.sql
```

### Missing Dependencies

```bash
# Clear Maven cache
mvn dependency:purge-local-repository

# Rebuild
mvn clean install
```

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Maven Documentation](https://maven.apache.org/guides/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)

## 🆘 Getting Help

If you encounter issues not covered here:

1. Check the logs in `logs/` directory
2. Review error messages in console
3. Verify all prerequisites are met
4. Contact the development team

---

**Setup Complete!** You should now be able to run the Hall Booking System on your local machine.

