-- Insert sample users
INSERT INTO users (username, password, email, full_name, phone, user_type, is_active) VALUES
('admin', 'admin123', 'admin@nadchathra.com', 'Admin User', '0112345678', 'ADMIN', TRUE),
('user', 'user123', 'user@example.com', 'John Doe', '0771234567', 'USER', TRUE),
('manager', 'manager123', 'manager@nadchathra.com', 'Event Manager', '0118765432', 'ADMIN', TRUE),
('user1', 'user123', 'user1@example.com', 'Test User 1', '0771111111', 'USER', TRUE),
('user2', 'user234', 'user2@example.com', 'Test User 2', '0772222222', 'USER', TRUE),
('user3', 'user345', 'user3@example.com', 'Test User 3', '0773333333', 'USER', TRUE),
('user4', 'user456', 'user4@example.com', 'Test User 4', '0774444444', 'USER', TRUE),
('user5', 'user567', 'user5@example.com', 'Test User 5', '0775555555', 'USER', TRUE);

-- Insert sample payment transactions
INSERT INTO payment_transactions (name, event_name, card_number, expiry_month, expiry_year, cvv, status, created_at)
VALUES 
('Alice Johnson', 'Wedding',   '1234567890123456', '12', '25', '123', 'COMPLETED', NOW()),
('Bob Smith', 'Birthday',  '9876543210987654', '08', '26', '456', 'FAILED', NOW()),
('Cara Wilson', 'Conference','1111222233334444', '05', '27', '789', 'PENDING', NOW());

-- Insert sample user requirements
INSERT INTO user_requirements (full_name, email, phone, event_type, event_date, guest_count, budget, special_requirements, card_number, expiry_month, expiry_year, cvv, card_holder_name, approval_status, user_id, cuisine_type, meal_type, dietary_restrictions, beverage_preferences, food_notes)
VALUES
    ('Alice Johnson', 'alice@example.com', '1234567890', 'Wedding', '2024-06-15', 150, 'LKR 500,000 - 1,000,000', 'Outdoor ceremony, vegetarian catering', '1234567890123456', '12', '25', '123', 'Alice Johnson', 'APPROVED', 2, 'Traditional South Indian', 'Lunch', 'basmathi rice, dhal curry, potato curry, brinjal curry, appalam, milaka poriyal, vada, paayasam, ice cream', 'paneer curry (200/per person), chicken curry (250/per person)', 'Spicy food preferred'),
    ('Bob Smith', 'bob@example.com', '2345678901', 'Birthday', '2024-03-20', 50, 'LKR 100,000 - 200,000', 'DJ required, cake cutting ceremony', '9876543210987654', '08', '26', '456', 'Bob Smith', 'PENDING', 2, 'Traditional South Indian', 'Lunch', 'normal rice, dhal curry, potato+tomato curry, fried brinjal curry, appalam, milaka poriyal, vada, paayasam, fruit salad', 'mutton curry (300/per person)', 'No special requirements'),
    ('Cara Wilson', 'cara@example.com', '3456789012', 'Conference', '2024-04-10', 200, 'LKR 200,000 - 500,000', 'AV equipment, lunch catering', '1111222233334444', '05', '27', '789', 'Cara Wilson', 'REJECTED', 2, 'Traditional South Indian', 'Lunch', 'basmathi rice, dhal curry, potato smashed, fried brinjal + ladies finger curry, appalam, milaka poriyal, vada, paayasam, ice cream', 'prawn curry (250/per person)', 'Halal food only'),
    ('Test User 1', 'user1@example.com', '1111111111', 'Wedding', '2024-12-01', 100, 'LKR 300,000 - 500,000', 'Garden wedding, live music', '1111111111111111', '01', '26', '111', 'Test User 1', 'PENDING', 4, 'Traditional South Indian', 'Lunch', 'basmathi rice, dhal curry, potato curry, brinjal curry, appalam, milaka poriyal, vada, paayasam, fruit salad', 'None', 'Traditional Sri Lankan cuisine'),
    ('Test User 2', 'user2@example.com', '2222222222', 'Birthday', '2024-11-15', 75, 'LKR 150,000 - 300,000', 'Kids party, entertainment', '2222222222222222', '02', '27', '222', 'Test User 2', 'PENDING', 5, 'Traditional South Indian', 'Lunch', 'normal rice, dhal curry, potato+tomato curry, brinjal curry, appalam, milaka poriyal, vada, paayasam, ice cream', 'None', 'Kid-friendly menu'),
    ('Test User 3', 'user3@example.com', '3333333333', 'Conference', '2024-10-20', 300, 'LKR 400,000 - 600,000', 'Corporate event, AV setup', '3333333333333333', '03', '28', '333', 'Test User 3', 'PENDING', 6, 'Traditional South Indian', 'Lunch', 'basmathi rice, dhal curry, potato curry, fried brinjal curry, appalam, milaka poriyal, vada, paayasam, fruit salad', 'paneer curry (200/per person), chicken curry (250/per person), mutton curry (300/per person)', 'Professional catering'),
    ('Test User 4', 'user4@example.com', '4444444444', 'Anniversary', '2024-09-30', 60, 'LKR 200,000 - 400,000', 'Romantic dinner, candlelight', '4444444444444444', '04', '29', '444', 'Test User 4', 'PENDING', 7, 'Traditional South Indian', 'Lunch', 'basmathi rice, dhal curry, potato+tomato curry, brinjal curry, appalam, milaka poriyal, vada, paayasam, ice cream', 'prawn curry (250/per person)', 'Fine dining experience'),
    ('Test User 5', 'user5@example.com', '5555555555', 'Graduation', '2024-08-25', 120, 'LKR 250,000 - 450,000', 'Celebration party, photo booth', '5555555555555555', '05', '30', '555', 'Test User 5', 'PENDING', 8, 'Traditional South Indian', 'Lunch', 'normal rice, dhal curry, potato smashed, brinjal curry, appalam, milaka poriyal, vada, paayasam, fruit salad', 'chicken curry (250/per person), mutton curry (300/per person)', 'Buffet style service');

-- Insert sample feedback
INSERT INTO feedback (user_requirement_id, user_id, overall_rating, venue_rating, service_rating, food_rating, staff_rating, comments, recommendation)
VALUES
(1, 2, 5, 5, 4, 5, 5, 'Excellent venue and service. Highly recommend!', 'yes'),
(2, 2, 4, 4, 4, 4, 4, 'Good overall experience with minor issues.', 'yes');
