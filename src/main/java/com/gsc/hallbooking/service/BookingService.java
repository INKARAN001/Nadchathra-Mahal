package com.gsc.hallbooking.service;

import com.gsc.hallbooking.entity.Booking;
import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    
    // Food prices per person
    private static final Map<String, Double> FOOD_PRICES = new HashMap<>();
    static {
        FOOD_PRICES.put("Green Delight", 500.0);      // Veg
        FOOD_PRICES.put("Royal Feast", 800.0);        // Non-Veg
        FOOD_PRICES.put("Spicy Grill", 750.0);        // Chicken
        FOOD_PRICES.put("Ocean Treasure", 1000.0);    // Seafood
    }
    
    public double calculateTotalAmount(String foodType, int peopleCount) {
        Double pricePerPerson = FOOD_PRICES.getOrDefault(foodType, 500.0);
        return pricePerPerson * peopleCount;
    }
    
    @Transactional
    public Booking createBooking(User user, String fullName, String phoneNumber, 
                                  String emailAddress, String hall, LocalDate bookingDate,
                                  String foodType, int peopleCount, String paymentType) {
        
        if (peopleCount > 800) {
            throw new RuntimeException("Maximum 800 people allowed");
        }
        
        if (bookingRepository.existsByHallAndBookingDate(hall, bookingDate)) {
            throw new RuntimeException("Hall is already booked for this date");
        }
        
        double totalAmount = calculateTotalAmount(foodType, peopleCount);
        
        Booking booking = Booking.builder()
                .user(user)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .emailAddress(emailAddress)
                .hall(hall)
                .bookingDate(bookingDate)
                .foodType(foodType)
                .peopleCount(peopleCount)
                .totalAmount(totalAmount)
                .paymentType(paymentType)
                .paymentStatus("PENDING")
                .bookingStatus("PENDING")
                .build();
        
        return bookingRepository.save(booking);
    }
    
    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByIdDesc(user);
    }
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByIdDesc();
    }
    
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
    
    @Transactional
    public void acceptBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setBookingStatus("ACCEPTED");
        bookingRepository.save(booking);
    }
    
    @Transactional
    public void rejectBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setBookingStatus("REJECTED");
        bookingRepository.save(booking);
    }
    
    @Transactional
    public void markPaymentAsPaid(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setPaymentStatus("PAID");
        booking.setBookingStatus("BOOKED");
        bookingRepository.save(booking);
    }
    
    public Map<String, Double> getFoodPrices() {
        return FOOD_PRICES;
    }
}

