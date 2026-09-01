# Project Structure

Detailed overview of the Hall Booking System project architecture and file organization.

## 📁 Directory Structure

```
Nadchathra-Mahal-main/
│
├── database/                           # Database scripts and migrations
│   ├── essential_schema.sql            # Core database schema
│   ├── recreate_database.sql           # Full database recreation with data
│   ├── recreate_database.bat           # Windows setup script
│   ├── recreate_database.sh            # Linux/Mac setup script
│   ├── schema.sql                      # Legacy schema
│   ├── fix_database.sql                # Database fixes
│   ├── migrate_split_tables.sql        # Migration script
│   ├── add_admin_table.sql             # Admin table creation
│   ├── add_login_info_table.sql        # Login tracking table
│   ├── add_messages_table.sql          # Messages table
│   ├── add_payment_receipt_table.sql   # Payment receipts table
│   ├── remove_login_datetime_column.sql
│   ├── reset_admin.sql                 # Reset admin credentials
│   └── README.md                        # Database documentation
│
├── src/
│   └── main/
│       ├── java/com/gsc/hallbooking/
│       │   ├── HallBookingApplication.java    # Main Spring Boot application
│       │   │
│       │   ├── config/                        # Configuration classes
│       │   │   ├── SecurityConfig.java        # Spring Security configuration
│       │   │   └── DataInitializer.java       # Initial data setup
│       │   │
│       │   ├── controller/                    # MVC Controllers
│       │   │   ├── HomeController.java        # Home page and public routes
│       │   │   ├── AuthController.java        # Authentication (login, signup)
│       │   │   ├── UserController.java        # User dashboard and operations
│       │   │   ├── AdminController.java        # Admin dashboard and operations
│       │   │   └── ManagerController.java      # Manager operations
│       │   │
│       │   ├── entity/                        # JPA Entity classes
│       │   │   ├── User.java                  # User entity
│       │   │   ├── Admin.java                 # Admin entity
│       │   │   ├── Hall.java                  # Hall entity
│       │   │   ├── FoodItem.java              # Food item entity
│       │   │   ├── Booking.java                # Booking entity
│       │   │   ├── BookingDetails.java        # Booking details entity
│       │   │   ├── Feedback.java              # Feedback entity
│       │   │   ├── Message.java               # Message entity
│       │   │   └── LoginInfo.java             # Login info entity
│       │   │
│       │   ├── repository/                    # Data repositories
│       │   │   ├── UserRepository.java        # User data operations
│       │   │   ├── AdminRepository.java       # Admin data operations
│       │   │   ├── HallRepository.java        # Hall data operations
│       │   │   ├── FoodItemRepository.java    # Food item operations
│       │   │   ├── BookingRepository.java     # Booking operations
│       │   │   ├── BookingDetailsRepository.java
│       │   │   ├── FeedbackRepository.java    # Feedback operations
│       │   │   ├── MessageRepository.java     # Message operations
│       │   │   └── LoginInfoRepository.java   # Login info operations
│       │   │
│       │   └── service/                       # Business logic layer
│       │       ├── UserService.java           # User business logic
│       │       ├── AdminService.java          # Admin business logic
│       │       ├── ManagerService.java        # Manager business logic
│       │       ├── BookingService.java        # Booking business logic
│       │       ├── FeedbackService.java       # Feedback operations
│       │       ├── MessageService.java        # Message operations
│       │       ├── LoginInfoService.java      # Login tracking
│       │       └── CustomUserDetailsService.java  # Spring Security integration
│       │
│       └── resources/
│           ├── application.properties         # Application configuration
│           │
│           ├── static/                        # Static resources
│           │   ├── css/
│           │   │   ├── style.css             # Main stylesheet
│           │   │   └── home-professional.css # Home page styles
│           │   ├── js/
│           │   │   ├── booking.js            # Booking JavaScript logic
│           │   │   ├── payment.js            # Payment handling
│           │   │   └── slider.js            # Image slider functionality
│           │   └── images/
│           │       ├── 1.jpg                # Image assets
│           │       ├── 2.jpg
│           │       └── 3.jpg
│           │
│           └── templates/                     # Thymeleaf templates
│               ├── home.html                  # Home page
│               ├── login.html                 # Login page
│               ├── signup.html                # Registration page
│               ├── halls.html                 # Hall listing
│               ├── events.html                # Events page
│               ├── food-menu.html             # Food menu
│               ├── contact.html               # Contact page
│               │
│               ├── admin/                     # Admin templates
│               │   ├── dashboard.html         # Admin dashboard
│               │   ├── users.html             # User management
│               │   ├── messages.html          # Message management
│               │   ├── send-message.html      # Send message
│               │   ├── view-message.html      # View message
│               │   ├── edit-message.html      # Edit message
│               │   └── feedbacks.html         # Feedback management
│               │
│               ├── manager/                   # Manager templates
│               │   ├── dashboard.html         # Manager dashboard
│               │   ├── halls.html             # Hall management
│               │   ├── hall-form.html         # Add hall
│               │   ├── hall-edit.html         # Edit hall
│               │   ├── foods.html             # Food management
│               │   ├── food-form.html         # Add food
│               │   └── food-edit.html         # Edit food
│               │
│               └── user/                     # User templates
│                   ├── dashboard.html         # User dashboard
│                   ├── profile.html           # User profile
│                   ├── profile-edit.html      # Edit profile
│                   ├── booking-stage1.html     # Booking stage 1
│                   ├── booking-stage2.html     # Booking stage 2
│                   ├── booking-view.html      # View booking
│                   ├── booking-edit.html       # Edit booking
│                   ├── payment-gateway.html    # Payment page
│                   ├── feedback.html          # Submit feedback
│                   └── messages.html          # User messages
│
├── target/                                   # Compiled output (auto-generated)
│   ├── classes/                            # Compiled Java classes
│   └── hall-booking-system-1.0.0.jar       # Build artifact
│
├── pom.xml                                   # Maven project configuration
├── run.bat                                   # Quick start script (Windows)
├── README.md                                 # Project overview
├── SETUP.md                                  # Setup instructions
├── CONFIGURATION.md                          # Configuration guide
├── DEPLOYMENT_GUIDE.md                      # Deployment guide
├── PROJECT_STRUCTURE.md                     # This file
└── .gitignore                               # Git ignore rules
```

## 🏗️ Architecture Overview

### MVC Pattern

The application follows the Model-View-Controller architecture:

- **Model**: Entity classes and JPA repositories
- **View**: Thymeleaf templates
- **Controller**: REST controllers handling HTTP requests

### Layer Structure

```
┌─────────────────────────────────────┐
│         Presentation Layer          │
│     (Controllers + Templates)       │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│         Business Logic Layer        │
│            (Services)                │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│          Data Access Layer          │
│     (Repositories + Entities)       │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│         Database Layer (MySQL)      │
└─────────────────────────────────────┘
```

## 🔑 Key Components

### 1. Controllers

#### HomeController
- Handles public pages (home, halls, events, food menu, contact)
- No authentication required

#### AuthController
- User registration
- Login/logout
- Password handling

#### UserController
- User dashboard
- Profile management
- Booking operations (view, create, edit)
- Payment processing
- Feedback submission
- Message management

#### AdminController
- Admin dashboard
- User management (view, approve, delete)
- Booking approval/rejection
- Feedback moderation
- Send/receive messages
- System statistics

#### ManagerController
- Manager dashboard
- Hall management (CRUD)
- Food item management (CRUD)
- Active status toggling

### 2. Entities and Relationships

```
User (1) ────< (Many) Booking
User (1) ────< (Many) Feedback
User (1) ────< (Many) Message (as sender)
User (1) ────< (Many) Message (as recipient)
User (1) ────< (Many) LoginInfo
User (1) ──── (1) Admin

Booking (1) ──── (1) BookingDetails
```

### 3. Database Schema

#### Core Tables

1. **users** - User accounts
   - id, name, email, password, phone, role, enabled, created_at

2. **admin** - Admin-specific data
   - id, user_id (FK), user_status, total_bookings

3. **halls** - Venue information
   - id, name, capacity, description, active

4. **food_items** - Menu items
   - id, name, price_per_person, description, category, active

5. **bookings** - Booking header
   - id, user_id (FK), full_name, phone_number, email_address, hall, booking_date

6. **booking_details** - Booking specifics
   - id, booking_id (FK), food_type, people_count, payment_type, total_amount, payment_status, booking_status

7. **feedback** - User feedback
   - id, user_id (FK), message, approved, created_at

8. **messages** - Internal messaging
   - id, sender_id (FK), recipient_id (FK), subject, content, message_type, is_read, created_at

9. **login_info** - Login tracking
   - id, user_id (FK), login_date, login_time

## 🔐 Security Implementation

### Spring Security Configuration
- Password encryption (BCrypt)
- Role-based access control (ADMIN, MANAGER, USER)
- Session management
- CSRF protection

### User Roles

1. **ADMIN**: Full system access
2. **MANAGER**: Hall and food management
3. **USER**: Book events, view profile

## 📊 Data Flow

### Booking Process Flow

```
1. User browses halls
   └─> HomeController.showHalls()
       └─> HallRepository.findAll()

2. User selects hall and date
   └─> UserController.showBookingStage1()
       └─> BookingService.createBooking()

3. User selects food and fills details
   └─> UserController.showBookingStage2()
       └─> BookingService.addBookingDetails()

4. Payment processing
   └─> UserController.processPayment()
       └─> BookingService.confirmPayment()

5. Admin approves
   └─> AdminController.approveBooking()
       └─> BookingService.updateStatus()
```

## 🎨 Frontend Structure

### CSS Files
- `style.css`: Global styles
- `home-professional.css`: Home page specific styles

### JavaScript Files
- `booking.js`: Booking wizard logic
- `payment.js`: Payment processing
- `slider.js`: Image carousel

### Template Organization
Templates are organized by role:
- Public templates (root)
- Admin templates (admin/)
- Manager templates (manager/)
- User templates (user/)

## 🔧 Configuration Files

### application.properties
Main configuration file containing:
- Server port
- Database connection
- JPA/Hibernate settings
- Thymeleaf configuration
- Logging levels

### pom.xml
Maven configuration defining:
- Spring Boot version
- Dependencies
- Build plugins
- Project metadata

## 🚀 Build Process

```
mvn clean install
    ↓
    ├─> Compile Java classes
    ├─> Process resources
    ├─> Run tests
    └─> Package JAR

Output: target/hall-booking-system-1.0.0.jar
```

## 📝 Developer Responsibilities

As per the code structure:

- **JADAVAN**: User management, authentication, security configuration
- **AKASH**: Booking system, payment processing, calendar functionality
- **SRIKARSAN**: Manager operations (halls, food items)
- **INKARAN**: Admin management, monitoring, messaging

## 🔄 Modification Guidelines

When modifying the project:

1. **Entities**: Update at `entity/` package
2. **Database changes**: Create migration script in `database/`
3. **Business logic**: Modify `service/` classes
4. **New endpoints**: Add to appropriate `controller/`
5. **UI changes**: Update `templates/` and `static/`
6. **Configuration**: Modify `application.properties`

## 📚 Additional Information

- **Main Application Class**: `HallBookingApplication.java`
- **Default Port**: 8081
- **Database**: MySQL 8.0+
- **Java Version**: 17
- **Spring Boot Version**: 3.2.0

---

This structure maintains separation of concerns and follows Spring Boot best practices for scalability and maintainability.

