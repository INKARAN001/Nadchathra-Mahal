# Database Migration Guide: Split Bookings Table

## Overview
This guide explains the database schema changes where the `bookings` table has been split into two tables for better normalization and organization.

## What Changed?

### Old Structure (Single Table)
Previously, all booking information was stored in one `bookings` table:
- Basic info: user_id, full_name, phone_number, email_address, hall, booking_date
- Details: food_type, people_count, payment_type, total_amount, payment_status, booking_status

### New Structure (Split Tables)

#### 1. `bookings` table (Basic Information)
Contains core booking information:
- `id` - Primary key
- `user_id` - Foreign key to users table
- `full_name` - Customer's full name
- `phone_number` - Contact number
- `email_address` - Email address
- `hall` - Hall name (Hall A, B, or C)
- `booking_date` - Date of the event

#### 2. `booking_details` table (Additional Information)
Contains booking specifics and status:
- `id` - Primary key
- `booking_id` - Foreign key to bookings table (ONE-TO-ONE relationship)
- `food_type` - Selected food menu
- `people_count` - Number of guests
- `payment_type` - CARD or CASH
- `total_amount` - Total booking amount
- `payment_status` - PENDING or PAID
- `booking_status` - PENDING, ACCEPTED, REJECTED, or BOOKED

## Benefits of This Change
1. **Better Organization**: Separates basic contact info from booking details
2. **Improved Performance**: Queries for basic info don't need to load all details
3. **Easier Maintenance**: Changes to payment/booking logic don't affect basic info
4. **Cleaner Code**: Follows database normalization principles

## Migration Steps

### For Fresh Installation
Simply run the updated schema:
```bash
mysql -u root -p < database/schema.sql
```

### For Existing Database with Data

**Important**: Backup your database first!

```bash
# Backup your database
mysqldump -u root -p hall_booking_db > backup_$(date +%Y%m%d).sql

# Run the migration script
mysql -u root -p < database/migrate_split_tables.sql
```

The migration script will:
1. Create a backup table (`bookings_backup`)
2. Create the new table structure
3. Migrate all existing data
4. Verify the migration
5. Keep the backup for safety

### Verify Migration
After running the migration, verify your data:
```sql
USE hall_booking_db;

-- Check row counts match
SELECT COUNT(*) FROM bookings;
SELECT COUNT(*) FROM booking_details;
SELECT COUNT(*) FROM bookings_backup;

-- All three should return the same count

-- Spot check some data
SELECT b.*, bd.* 
FROM bookings b 
JOIN booking_details bd ON b.id = bd.booking_id 
LIMIT 5;
```

## Code Changes

### Entity Classes
- **Booking.java**: Removed detail fields, added `OneToOne` relationship to `BookingDetails`
- **BookingDetails.java**: New entity class for booking details

### Repository
- **BookingDetailsRepository.java**: New repository for booking details operations

### Service Layer
- **BookingService.java**: Updated to create both `Booking` and `BookingDetails` entities
- **PaymentService.java**: Updated to access amount from `booking.getBookingDetails().getTotalAmount()`

### Controllers
- **UserController.java**: Updated to access fields via `booking.getBookingDetails()`
- **AdminController.java**: No changes needed (uses service methods)

### Templates (Thymeleaf)
All templates updated to use `booking.bookingDetails.fieldName` instead of `booking.fieldName`:
- `user/dashboard.html`
- `user/payment-gateway.html`
- `admin/dashboard.html`

## Rollback Plan
If you need to rollback:

```sql
USE hall_booking_db;

-- Restore from backup
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS booking_details;

-- Recreate old structure from backup
CREATE TABLE bookings AS SELECT * FROM bookings_backup;

-- Remove backup
DROP TABLE bookings_backup;
```

## Testing Checklist
After migration, test these features:
- [ ] User can create new bookings
- [ ] Admin can view all bookings
- [ ] Admin can accept/reject bookings
- [ ] User can make card payments
- [ ] Admin can mark cash payments as paid
- [ ] Booking details display correctly on dashboards
- [ ] Payment gateway shows correct amounts
- [ ] All booking statuses work correctly

## Support
If you encounter any issues during migration:
1. Check MySQL error logs
2. Verify all foreign key constraints
3. Ensure JPA entities are properly loaded
4. Restart the Spring Boot application after database changes

## Clean Up
Once you've verified everything works correctly (after a few days):
```sql
-- Remove the backup table
DROP TABLE IF EXISTS bookings_backup;
```

