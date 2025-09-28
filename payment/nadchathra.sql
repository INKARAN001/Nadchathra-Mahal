-- ==============================
-- Database
-- ==============================
CREATE DATABASE IF NOT EXISTS nadchathra;
USE nadchathra;

-- ==============================
-- Bookings Table
-- ==============================
CREATE TABLE IF NOT EXISTS Bookings (
    booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    event_name VARCHAR(50) NOT NULL,
    hall VARCHAR(50) NOT NULL,
    expected_guests INT NOT NULL,
    event_date DATE NOT NULL,
    total_price DECIMAL(10,2) DEFAULT 0.00,
    status VARCHAR(50) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ==============================
-- Payments Table
-- ==============================
CREATE TABLE IF NOT EXISTS Payments (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    card_last4 CHAR(4) NOT NULL,
    expiry_month CHAR(2) NOT NULL,
    expiry_year CHAR(2) NOT NULL,
    cvv_hash VARCHAR(255) NOT NULL,
    payment_status VARCHAR(50) DEFAULT 'Validated',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payments_booking
        FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==============================
-- Gallery Table
-- ==============================
CREATE TABLE IF NOT EXISTS Gallery (
    image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- ==============================
-- Food Menu Table
-- ==============================
CREATE TABLE IF NOT EXISTS FoodMenu (
    food_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    food_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) DEFAULT 0.00,
    available BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;

-- ==============================
-- Booking-Food Many-to-Many Table
-- ==============================
CREATE TABLE IF NOT EXISTS Booking_Food (
    booking_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    PRIMARY KEY (booking_id, food_id),
    CONSTRAINT fk_booking_food_booking
        FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_booking_food_food
        FOREIGN KEY (food_id) REFERENCES FoodMenu(food_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==============================
-- Feedback Table
-- ==============================
CREATE TABLE IF NOT EXISTS Feedback (
    feedback_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    message TEXT,
    rating INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_feedback_booking
        FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;
