# Database Recreation Summary

## What You Need to Create

To recreate the database for the Hall Booking System with all the updated code and developer credits, you need to create the following:

### 1. Database Tables (9 tables total)

#### Core User Management (JADAVAN)
- **users** - Core user data (id, name, email, password, phone, role, enabled)
- **admin** - Admin management (id, user_id, user_status, total_bookings)

#### Admin Management & Monitoring (INKARAN)  
- **login_info** - Login tracking (id, user_id, login_date, login_time)
- **feedback** - User feedback (id, user_id, message, approved, created_at)
- **messages** - Messaging system (id, sender_id, recipient_id, subject, content, message_type, is_read, created_at)

#### Manager Operations (SRIKARSAN)
- **halls** - Hall management (id, name, capacity, description, active)
- **food_items** - Food item management (id, name, price_per_person, description, category, active)

#### Booking & Payment System (AKASH)
- **bookings** - Basic booking info (id, user_id, full_name, phone_number, email_address, hall, booking_date)
- **booking_details** - Detailed booking info including payment (id, booking_id, food_type, people_count, payment_type, total_amount, payment_status, booking_status)

### 2. Database Scripts Created

#### Complete Recreation Script
- **recreate_database.sql** - Full database recreation with sample data
- **recreate_database.bat** - Windows batch script for easy execution
- **recreate_database.sh** - Linux/Mac shell script for easy execution

#### Essential Schema Only
- **essential_schema.sql** - Clean schema without sample data

#### Documentation
- **DATABASE_RECREATION_GUIDE.md** - Complete guide with instructions
- **MIGRATION_GUIDE.md** - Migration guide for existing databases

### 3. Default Data Included

#### Users
- Admin: admin@gsc.com / admin123
- Manager: manager@gsc.com / manager123

#### Halls
- Hall 1 (800 capacity)
- Hall 2 (800 capacity)
- Garden Venue (Outdoor) (800 capacity)

#### Food Items
- Green Delight (VEG) - ₹500/person
- Royal Feast (NON_VEG) - ₹800/person
- Spicy Grill (NON_VEG) - ₹750/person
- Ocean Treasure (SEAFOOD) - ₹1000/person

### 4. Key Features Supported

#### User Management (JADAVAN)
- User registration and authentication
- Profile management
- Account deletion with proper foreign key handling

#### Booking System (AKASH)
- Multi-stage booking process
- Calendar integration
- Payment processing
- Booking status management

#### Manager Operations (SRIKARSAN)
- Hall management (CRUD)
- Food item management (CRUD)
- Active/inactive status management

#### Admin Management (INKARAN)
- Booking approval/rejection
- User management
- Feedback moderation
- Messaging system
- Login tracking

### 5. Database Relationships

- users → admin (One-to-One)
- users → bookings (One-to-Many)
- users → feedback (One-to-Many)
- users → messages (One-to-Many, sender/recipient)
- users → login_info (One-to-Many)
- bookings → booking_details (One-to-One)

### 6. How to Use

#### Quick Start (Windows)
```bash
# Run the batch script
database\recreate_database.bat
```

#### Quick Start (Linux/Mac)
```bash
# Run the shell script
./database/recreate_database.sh
```

#### Manual Execution
```bash
# Complete recreation with sample data
mysql -u root -p < database/recreate_database.sql

# Or essential schema only
mysql -u root -p < database/essential_schema.sql
```

### 7. Verification

After recreation, verify:
1. All 9 tables exist
2. Default users can login
3. Default halls and food items are available
4. Spring Boot application starts without errors
5. All CRUD operations work correctly

### 8. Developer Credits

All database scripts include proper developer credits:
- **JADAVAN**: Core user management, authentication, system configuration
- **AKASH**: Booking system, payment processing, calendar functionality  
- **SRIKARSAN**: Manager operations (halls, food items)
- **INKARAN**: Admin management, monitoring, messaging, user management

The database is now fully aligned with the updated codebase and includes all necessary tables, relationships, and default data for the Hall Booking System to function properly.
