# Hall Booking System

A comprehensive Hall Booking Management System built with Spring Boot, providing a platform for booking event halls, managing reservations, handling payments, and admin management.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Default Credentials](#default-credentials)
- [Project Structure](#project-structure)
- [Documentation](#documentation)
- [Developer Credits](#developer-credits)

## ✨ Features

### User Features
- User registration and authentication
- Hall browsing and selection
- Multi-stage booking process
- Payment processing
- Profile management
- View and edit bookings
- Submit feedback
- Receive messages from admin

### Manager Features
- Hall management (CRUD operations)
- Food item management (CRUD operations)
- Activate/deactivate halls and food items
- Dashboard with statistics

### Admin Features
- User management (view, approve, delete users)
- Booking management and approval
- Feedback moderation
- Messaging system
- Login tracking
- View system statistics

## 🛠 Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Database**: MySQL
- **Security**: Spring Security
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Java Version**: 17
- **Frontend**: HTML, CSS, JavaScript

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java JDK 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git** (for version control)

### Check Your Versions

```bash
java -version
mvn -version
mysql --version
```

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Nadchathra-Mahal-main
```

### 2. Set Up Database

#### Option A: Using Provided Script (Recommended)

**Windows:**
```bash
cd database
recreate_database.bat
```

**Linux/Mac:**
```bash
cd database
chmod +x recreate_database.sh
./recreate_database.sh
```

#### Option B: Manual Setup

```bash
mysql -u root -p
```

Then run the SQL scripts in order:
```sql
source database/recreate_database.sql;
```

### 3. Configure Application

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 4. Run the Application

**Option A: Using run.bat (Windows)**
```bash
run.bat
```

**Option B: Using Maven**
```bash
mvn clean install
mvn spring-boot:run
```

**Option C: Build and Run JAR**
```bash
mvn clean package
java -jar target/hall-booking-system-1.0.0.jar
```

### 5. Access the Application

Once the application starts, open your browser and navigate to:

```
http://localhost:8081
```

## 🔐 Default Credentials

### Admin Account
- **Email**: admin@gsc.com
- **Password**: admin123

### Manager Account
- **Email**: manager@gsc.com
- **Password**: manager123

### Test User
You can create additional users through the registration page.

## 📁 Project Structure

```
Nadchathra-Mahal-main/
├── src/
│   └── main/
│       ├── java/com/gsc/hallbooking/
│       │   ├── config/          # Configuration classes
│       │   ├── controller/      # MVC Controllers
│       │   ├── entity/          # JPA Entities
│       │   ├── repository/      # Data repositories
│       │   └── service/         # Business logic
│       └── resources/
│           ├── templates/       # Thymeleaf templates
│           ├── static/          # CSS, JS, images
│           └── application.properties
├── database/                    # Database scripts
├── target/                      # Compiled files (auto-generated)
├── pom.xml                      # Maven configuration
├── run.bat                      # Quick start script
└── README.md                    # This file
```

## 📚 Documentation

- [Setup Guide](SETUP.md) - Detailed installation instructions
- [Configuration Guide](CONFIGURATION.md) - Configuration options
- [Deployment Guide](DEPLOYMENT_GUIDE.md) - Production deployment
- [Project Structure](PROJECT_STRUCTURE.md) - Detailed project architecture
- [Database Setup](database/README.md) - Database documentation

## 👨‍💻 Developer Credits

This project was developed by:

- **JADAVAN** - Core user management, authentication, system configuration
- **AKASH** - Booking system, payment processing, calendar functionality
- **SRIKARSAN** - Manager operations (halls, food items management)
- **INKARAN** - Admin management, monitoring, messaging, user management

## 🗄️ Database Schema

The application uses 9 main tables:

1. **users** - Core user data
2. **admin** - Admin management
3. **halls** - Hall information
4. **food_items** - Food menu items
5. **bookings** - Booking basic info
6. **booking_details** - Detailed booking with payment info
7. **feedback** - User feedback
8. **messages** - Messaging system
9. **login_info** - Login tracking

## 🔧 Common Issues

### Port Already in Use

If port 8081 is already in use, change it in `application.properties`:

```properties
server.port=8082
```

### Database Connection Error

1. Verify MySQL is running
2. Check database credentials in `application.properties`
3. Ensure database exists: `hall_booking_db`

### Build Errors

```bash
mvn clean
mvn compile
```

## 📝 License

This project is proprietary software. All rights reserved.

## 🤝 Support

For issues and questions, please contact the development team.

---

**Version**: 1.0.0  
**Last Updated**: 2024

