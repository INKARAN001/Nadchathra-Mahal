# 🎉 Nadchathra Mahal - Hall Booking System

A comprehensive **Hall Booking Management System** built with Spring Boot and modern web technologies. This platform enables users to browse, book event halls, manage reservations, process payments, and submit feedback. Managers can manage halls and food items, while administrators oversee the entire system.

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Maven](https://img.shields.io/badge/Maven-3.8.1-C71A36?style=for-the-badge&logo=apache-maven)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.0-green?style=for-the-badge)

</div>

---

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Default Credentials](#-default-credentials)
- [Project Structure](#-project-structure)
- [Configuration](#-configuration)
- [Database Setup](#-database-setup)
- [API Endpoints](#-api-endpoints)
- [Key Features Explained](#-key-features-explained)
- [Deployment](#-deployment)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)
- [Support](#-support)

---

## ✨ Features

### 👤 User Features
- ✅ User registration and authentication with secure login
- ✅ Browse halls with detailed descriptions and images
- ✅ View available time slots and capacities
- ✅ Multi-stage booking process (hall selection → details → confirmation)
- ✅ Secure payment processing
- ✅ View and manage your bookings
- ✅ Edit booking details before approval
- ✅ Submit feedback and ratings
- ✅ Receive notifications from administrators
- ✅ View personal profile and booking history
- ✅ Cancel bookings with automatic refunds

### 👨‍💼 Manager Features
- 📊 Dashboard with key statistics
- 🏛️ Hall management (Create, Read, Update, Delete)
- 🍽️ Food item management (Create, Read, Update, Delete)
- ✨ Activate/deactivate halls and food items
- 📈 View hall availability and booking analytics
- 🔍 Track food item popularity

### 🛡️ Admin Features
- 👥 User management (view, approve, reject, delete users)
- 📅 Booking management and approval workflow
- ⭐ Feedback moderation and review
- 💬 Messaging system for user communication
- 🔐 Login tracking and security monitoring
- 📊 System-wide statistics and analytics
- 🚨 System health monitoring

---

## 🛠 Technology Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Spring Boot 3.2.0, Spring MVC |
| **Security** | Spring Security, JWT (optional) |
| **Database** | MySQL 8.0 |
| **ORM** | Spring Data JPA, Hibernate |
| **Template Engine** | Thymeleaf |
| **Frontend** | HTML5, CSS3, JavaScript (ES6) |
| **Build Tool** | Maven 3.8.1 |
| **Java Version** | JDK 17 |

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK)**: 17 or higher
  ```bash
  java -version
  ```
- **Maven**: 3.8.1 or higher
  ```bash
  mvn -version
  ```
- **MySQL**: 8.0 or higher
  ```bash
  mysql --version
  ```
- **Git**: For version control
  ```bash
  git --version
  ```

### System Requirements
- **RAM**: Minimum 2GB (4GB recommended)
- **Disk Space**: 500MB for application and dependencies
- **OS**: Windows, macOS, or Linux

---

## 🚀 Quick Start

### Step 1: Clone the Repository

```bash
git clone https://github.com/INKARAN001/Nadchathra-Mahal.git
cd Nadchathra-Mahal
```

### Step 2: Configure Database

1. Open MySQL and create a new database:
```sql
CREATE DATABASE hall_booking_db;
```

2. Execute the database initialization script:
   - **Windows**: Double-click `database/recreate_database.bat`
   - **Linux/Mac**: Run `bash database/recreate_database.sh`
   - **Manual**: Import `database/essential_schema.sql` into your MySQL client

### Step 3: Update Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hall_booking_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.check-template-location=true

# Server Configuration
server.port=8080
server.servlet.context-path=/
```

### Step 4: Build the Project

```bash
mvn clean install
```

### Step 5: Run the Application

#### Option A: Using Maven
```bash
mvn spring-boot:run
```

#### Option B: Using Java
```bash
java -jar target/hall-booking-system-1.0.0.jar
```

#### Option C: Windows Batch File
```bash
run.bat
```

### Step 6: Access the Application

Open your browser and navigate to:
```
http://localhost:8080/
```

---

## 🔐 Default Credentials

### 👨‍💼 Admin Account
- **Username/Email**: `admin@hallbooking.com`
- **Password**: `admin123`
- **Role**: Administrator (Full system access)

### 👨‍💼 Manager Account
- **Username/Email**: `manager@hallbooking.com`
- **Password**: `manager123`
- **Role**: Manager (Hall and food management)

### 👤 Demo User Account
- **Username/Email**: `user@example.com`
- **Password**: `user123`
- **Role**: Regular User

**⚠️ Security Warning**: Change default credentials immediately after first login in production environments.

---

## 📂 Project Structure

```
Nadchathra-Mahal/
│
├── 📁 database/                          # Database scripts and migrations
│   ├── essential_schema.sql              # Core database schema
│   ├── recreate_database.sql             # Full database recreation
│   ├── recreate_database.bat/.sh         # Setup scripts
│   └── README.md                         # Database documentation
│
├── 📁 docs/                              # Project documentation
│   ├── CONFIGURATION.md                  # Configuration guide
│   ├── DEPLOYMENT_GUIDE.md               # Deployment instructions
│   ├── SETUP.md                          # Setup instructions
│   └── PROJECT_STRUCTURE.md              # Detailed architecture
│
├── 📁 src/
│   └── main/
│       ├── 📁 java/com/gsc/hallbooking/
│       │   ├── HallBookingApplication.java           # Main application class
│       │   │
│       │   ├── 📁 config/                            # Configuration classes
│       │   │   ├── SecurityConfig.java               # Spring Security setup
│       │   │   └── DataInitializer.java              # Initial data loading
│       │   │
│       │   ├── 📁 controller/                        # MVC Controllers
│       │   │   ├── HomeController.java               # Public pages
│       │   │   ├── AuthController.java               # Login/Register
│       │   │   ├── UserController.java               # User operations
│       │   │   ├── AdminController.java              # Admin operations
│       │   │   └── ManagerController.java            # Manager operations
│       │   │
│       │   ├── 📁 entity/                            # JPA Entities
│       │   │   ├── User.java                         # User entity
│       │   │   ├── Hall.java                         # Hall entity
│       │   │   ├── Booking.java                      # Booking entity
│       │   │   ├── FoodItem.java                     # Food menu item
│       │   │   ├── Feedback.java                     # User feedback
│       │   │   ├── Message.java                      # Admin messages
│       │   │   ├── Admin.java                        # Admin entity
│       │   │   ├── LoginInfo.java                    # Login tracking
│       │   │   └── BookingDetails.java               # Booking details
│       │   │
│       │   ├── 📁 repository/                        # Data Access Layer
│       │   │   ├── UserRepository.java               # User data operations
│       │   │   ├── HallRepository.java               # Hall data operations
│       │   │   ├── BookingRepository.java            # Booking operations
│       │   │   ├── FoodItemRepository.java           # Food item operations
│       │   │   ├── FeedbackRepository.java           # Feedback operations
│       │   │   ├── MessageRepository.java            # Message operations
│       │   │   ├── AdminRepository.java              # Admin operations
│       │   │   └── LoginInfoRepository.java          # Login tracking
│       │   │
│       │   └── 📁 service/                           # Business Logic Layer
│       │       ├── UserService.java                  # User management
│       │       ├── HallService.java                  # Hall management
│       │       ├── BookingService.java               # Booking operations
│       │       ├── AdminService.java                 # Admin functions
│       │       ├── ManagerService.java               # Manager functions
│       │       ├── FeedbackService.java              # Feedback handling
│       │       ├── MessageService.java               # Message handling
│       │       ├── LoginInfoService.java             # Login tracking
│       │       └── CustomUserDetailsService.java     # User authentication
│       │
│       └── 📁 resources/
│           ├── application.properties                # Configuration
│           │
│           ├── 📁 static/                            # Static resources
│           │   ├── 📁 css/                           # Stylesheets
│           │   │   ├── style.css                     # Main styles
│           │   │   └── home-professional.css        # Home page styles
│           │   │
│           │   ├── 📁 js/                            # JavaScript files
│           │   │   ├── booking.js                    # Booking logic
│           │   │   ├── payment.js                    # Payment handling
│           │   │   └── slider.js                     # Image slider
│           │   │
│           │   └── 📁 images/                        # Image assets
│           │       ├── 1.jpg, 2.jpg, 3.jpg          # Hall images
│           │       └── ...
│           │
│           └── 📁 templates/                         # Thymeleaf templates
│               ├── home.html                         # Home page
│               ├── login.html                        # Login form
│               ├── signup.html                       # Registration form
│               ├── halls.html                        # Hall listing
│               ├── contact.html                      # Contact page
│               │
│               ├── 📁 admin/                         # Admin templates
│               │   ├── dashboard.html                # Admin dashboard
│               │   ├── users.html                    # User management
│               │   ├── messages.html                 # Message center
│               │   ├── feedbacks.html                # Feedback review
│               │   └── ...
│               │
│               ├── 📁 manager/                       # Manager templates
│               │   ├── dashboard.html                # Manager dashboard
│               │   ├── halls.html                    # Hall management
│               │   ├── foods.html                    # Food management
│               │   └── ...
│               │
│               └── 📁 user/                          # User templates
│                   ├── dashboard.html                # User dashboard
│                   ├── booking-stage1.html           # Booking step 1
│                   ├── booking-stage2.html           # Booking step 2
│                   ├── profile.html                  # User profile
│                   └── ...
│
├── 📄 pom.xml                            # Maven configuration
├── 📄 run.bat                            # Windows startup script
├── 📄 .gitignore                         # Git ignore file
├── 📄 README.md                          # This file
└── 📄 LICENSE                            # License file

```

---

## ⚙️ Configuration

### Application Properties

The main configuration file is located at: `src/main/resources/application.properties`

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hall_booking_db
spring.datasource.username=root
spring.datasource.password=your_password

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.check-template-location=true

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin123
```

For detailed configuration options, see [docs/CONFIGURATION.md](docs/CONFIGURATION.md).

---

## 💾 Database Setup

### Initial Setup

Run the database setup script:

**Windows:**
```bash
cd database
recreate_database.bat
```

**Linux/macOS:**
```bash
cd database
bash recreate_database.sh
```

**Manual Setup:**
```bash
mysql -u root -p < database/essential_schema.sql
```

### Database Backup

```bash
mysqldump -u root -p hall_booking_db > backup.sql
```

### Database Restore

```bash
mysql -u root -p hall_booking_db < backup.sql
```

For more database information, see [docs/SETUP.md](docs/SETUP.md).

---

## 🔗 API Endpoints

### Public Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Home page |
| GET | `/halls` | Browse all halls |
| GET | `/login` | Login page |
| POST | `/register` | User registration |
| GET | `/contact` | Contact page |

### User Endpoints (Authenticated)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/user/dashboard` | User dashboard |
| GET | `/user/profile` | View profile |
| POST | `/user/profile/update` | Update profile |
| GET | `/user/bookings` | View bookings |
| POST | `/user/booking/create` | Create booking |
| POST | `/user/booking/{id}/cancel` | Cancel booking |
| POST | `/user/feedback` | Submit feedback |

### Admin Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/admin/dashboard` | Admin dashboard |
| GET | `/admin/users` | Manage users |
| GET | `/admin/bookings` | Manage bookings |
| POST | `/admin/booking/{id}/approve` | Approve booking |
| POST | `/admin/booking/{id}/reject` | Reject booking |
| GET | `/admin/feedbacks` | View feedbacks |
| GET | `/admin/messages` | Message center |

### Manager Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/manager/dashboard` | Manager dashboard |
| GET | `/manager/halls` | Hall management |
| POST | `/manager/hall/create` | Add new hall |
| GET | `/manager/foods` | Food management |
| POST | `/manager/food/create` | Add food item |

---

## 🎯 Key Features Explained

### Multi-Stage Booking Process
Users go through a 3-step booking process:
1. **Stage 1**: Select hall and date
2. **Stage 2**: Choose amenities, add-ons, and food preferences
3. **Stage 3**: Review and confirm booking

### Payment Integration
- Secure payment processing
- Multiple payment methods (credit card, debit card, etc.)
- Payment confirmation and receipt generation
- Automatic refunds for canceled bookings

### Admin Approval Workflow
1. User submits booking
2. Admin reviews booking details
3. Admin approves or rejects booking
4. User receives confirmation notification
5. Payment processing begins after approval

### Feedback System
- Users can submit ratings and feedback
- Admin can moderate and respond to feedback
- Average ratings displayed on hall listings

### Messaging System
- Admins can send announcements to users
- Users receive notifications
- Message history tracking

---

## 📚 Documentation

- [Setup Guide](docs/SETUP.md) - Detailed installation instructions
- [Configuration Guide](docs/CONFIGURATION.md) - Configuration options
- [Deployment Guide](docs/DEPLOYMENT_GUIDE.md) - Production deployment
- [Project Structure](docs/PROJECT_STRUCTURE.md) - Detailed project architecture
- [Database Documentation](database/README.md) - Database documentation

---

## 📦 Deployment

### Development Environment
```bash
mvn clean install
mvn spring-boot:run
```

### Production Environment

For detailed deployment instructions, including Docker containerization and cloud deployment, see [docs/DEPLOYMENT_GUIDE.md](docs/DEPLOYMENT_GUIDE.md).

**Quick Production Build:**
```bash
mvn clean package -P production
java -jar target/hall-booking-system-1.0.0.jar
```

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Database Connection Error
```
Error: java.sql.SQLException: Access denied for user 'root'@'localhost'
```
**Solution**: Update your MySQL password in `application.properties`

#### 2. Port Already in Use
```
Error: Address already in use
```
**Solution**: Change port in `application.properties`:
```properties
server.port=8081
```

#### 3. Maven Build Failure
```
[ERROR] Failed to execute goal
```
**Solution**: Clear Maven cache:
```bash
mvn clean install -U
```

#### 4. Thymeleaf Template Error
```
Error: Could not parse as expression
```
**Solution**: Check HTML template syntax and variable names

For more troubleshooting tips, see [docs/SETUP.md](docs/SETUP.md).

---

## 🤝 Contributing

We welcome contributions from the community! Here's how you can help:

1. **Fork the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/Nadchathra-Mahal.git
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes**
   - Follow the existing code style
   - Add comments for complex logic
   - Update documentation as needed

4. **Commit your changes**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   ```

5. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Submit a Pull Request**
   - Provide a clear description of your changes
   - Reference any related issues

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 📞 Support

### Getting Help

- 📧 **Email**: admin@nadchathramahal.com
- 🐛 **Issues**: [GitHub Issues](https://github.com/INKARAN001/Nadchathra-Mahal/issues)
- 📚 **Documentation**: [docs/](docs/)
- 💬 **Discussions**: [GitHub Discussions](https://github.com/INKARAN001/Nadchathra-Mahal/discussions)

### Project Links

- **Repository**: https://github.com/INKARAN001/Nadchathra-Mahal
- **Issue Tracker**: https://github.com/INKARAN001/Nadchathra-Mahal/issues
- **Wiki**: https://github.com/INKARAN001/Nadchathra-Mahal/wiki

---

## 👨‍💻 Developer Credits

This project was developed as a comprehensive Hall Booking Management System with contributions from multiple developers:

- **JADAVAN** - Core user management, authentication, system configuration
- **AKASH** - Booking system, payment processing, calendar functionality
- **SRIKARSAN** - Manager operations (halls, food items management)
- **INKARAN** - Admin management, monitoring, messaging, user management

---

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

---

<div align="center">

**[⬆ back to top](#-nadchathra-mahal---hall-booking-system)**

Made with ❤️ by the Nadchathra Mahal Team

**Version**: 1.0.0 | **Last Updated**: 2024

</div>

