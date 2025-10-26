package com.gsc.hallbooking.service;

import com.gsc.hallbooking.entity.Admin;
import com.gsc.hallbooking.entity.Booking;
import com.gsc.hallbooking.entity.BookingDetails;
import com.gsc.hallbooking.entity.FoodItem;
import com.gsc.hallbooking.entity.Hall;
import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.repository.AdminRepository;
import com.gsc.hallbooking.repository.BookingRepository;
import com.gsc.hallbooking.repository.BookingDetailsRepository;
import com.gsc.hallbooking.repository.FoodItemRepository;
import com.gsc.hallbooking.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DEVELOPER: AKASH
 * CRUD: Booking Management System
 */
@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final BookingDetailsRepository bookingDetailsRepository;
    private final AdminRepository adminRepository;
    private final HallRepository hallRepository;
    private final FoodItemRepository foodItemRepository;
    
    // READ - Get all active halls for booking
    public List<Hall> getActiveHalls() {
        return hallRepository.findByActiveTrue();
    }
    
    // READ - Get all active food items for booking
    public List<FoodItem> getActiveFoodItems() {
        return foodItemRepository.findByActiveTrue();
    }
    
    // READ - Get food prices map
    public Map<String, Double> getFoodPrices() {
        Map<String, Double> prices = new HashMap<>();
        List<FoodItem> foodItems = getActiveFoodItems();
        for (FoodItem item : foodItems) {
            prices.put(item.getName(), item.getPricePerPerson());
        }
        return prices;
    }
    
    public double calculateTotalAmount(String foodType, int peopleCount) {
        // Try to get price from database first
        Optional<FoodItem> foodItem = foodItemRepository.findByName(foodType);
        if (foodItem.isPresent()) {
            return foodItem.get().getPricePerPerson() * peopleCount;
        }
        
        // Fallback to hardcoded prices for backward compatibility
        Map<String, Double> fallbackPrices = new HashMap<>();
        fallbackPrices.put("Green Delight", 500.0);
        fallbackPrices.put("Royal Feast", 800.0);
        fallbackPrices.put("Spicy Grill", 750.0);
        fallbackPrices.put("Ocean Treasure", 1000.0);
        
        Double pricePerPerson = fallbackPrices.getOrDefault(foodType, 500.0);
        return pricePerPerson * peopleCount;
    }
    
    // CREATE - Create new booking
    @Transactional
    public Booking createBooking(User user, String fullName, String phoneNumber, 
                                  String emailAddress, String hall, LocalDate bookingDate,
                                  String foodType, int peopleCount, String paymentType) {
        
        if (peopleCount > 800) {
            throw new RuntimeException("Maximum 800 people allowed");
        }
        
        // Allow multiple bookings for same date/hall
        // Admin will decide which one to accept
        
        double totalAmount = calculateTotalAmount(foodType, peopleCount);
        
        // Create the booking first
        Booking booking = Booking.builder()
                .user(user)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .emailAddress(emailAddress)
                .hall(hall)
                .bookingDate(bookingDate)
                .build();
        
        Booking savedBooking = bookingRepository.save(booking);
        
        // Create the booking details
        BookingDetails bookingDetails = BookingDetails.builder()
                .booking(savedBooking)
                .foodType(foodType)
                .peopleCount(peopleCount)
                .totalAmount(totalAmount)
                .paymentType(paymentType)
                .paymentStatus("PENDING")
                .bookingStatus("PENDING")
                .build();
        
        bookingDetailsRepository.save(bookingDetails);
        savedBooking.setBookingDetails(bookingDetails);
        
        // Update admin table - track total bookings for this user
        updateUserBookingCount(user);
        
        return savedBooking;
    }
    
    /**
     * Update or create admin record to track user's total bookings
     */
    @Transactional
    public void updateUserBookingCount(User user) {
        Optional<Admin> adminOptional = adminRepository.findByUserId(user.getId());
        
        if (adminOptional.isPresent()) {
            // Update existing admin record
            Admin admin = adminOptional.get();
            admin.setTotalBookings(admin.getTotalBookings() + 1);
            adminRepository.save(admin);
        } else {
            // Create new admin record for this user
            Admin newAdmin = Admin.builder()
                    .user(user)
                    .userStatus("ACTIVE")
                    .totalBookings(1)
                    .build();
            adminRepository.save(newAdmin);
        }
    }
    
    // READ - Get user bookings
    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByIdDesc(user);
    }
    
    // READ - Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByIdDesc();
    }
    
    // READ - Get booking by ID
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
    
    @Transactional
    public void acceptBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        BookingDetails details = booking.getBookingDetails();
        if (details == null) {
            details = bookingDetailsRepository.findByBookingId(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking details not found"));
        }
        details.setBookingStatus("ACCEPTED");
        bookingDetailsRepository.save(details);
        
        // Auto-reject all other PENDING bookings for the same hall and date
        String hall = booking.getHall();
        LocalDate date = booking.getBookingDate();
        
        List<Booking> conflictingBookings = bookingRepository.findAll().stream()
                .filter(b -> !b.getId().equals(bookingId)) // Exclude the accepted booking
                .filter(b -> hall.equals(b.getHall()) && date.equals(b.getBookingDate()))
                .filter(b -> {
                    // Only auto-reject PENDING bookings
                    if (b.getBookingDetails() != null) {
                        return "PENDING".equals(b.getBookingDetails().getBookingStatus());
                    }
                    return false;
                })
                .toList();
        
        // Reject all conflicting PENDING bookings
        for (Booking conflictingBooking : conflictingBookings) {
            BookingDetails conflictingDetails = conflictingBooking.getBookingDetails();
            if (conflictingDetails != null) {
                conflictingDetails.setBookingStatus("REJECTED");
                conflictingDetails.setPaymentStatus("CANCELLED");
                bookingDetailsRepository.save(conflictingDetails);
            }
        }
    }
    
    @Transactional
    public void rejectBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        BookingDetails details = booking.getBookingDetails();
        if (details == null) {
            details = bookingDetailsRepository.findByBookingId(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking details not found"));
        }
        details.setBookingStatus("REJECTED");
        details.setPaymentStatus("CANCELLED");
        bookingDetailsRepository.save(details);
    }
    
    @Transactional
    public void markPaymentAsPaid(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        BookingDetails details = booking.getBookingDetails();
        if (details == null) {
            details = bookingDetailsRepository.findByBookingId(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking details not found"));
        }
        details.setPaymentStatus("PAID");
        details.setBookingStatus("BOOKED");
        bookingDetailsRepository.save(details);
    }
    
    
    // Booking CRUD Operations for Users
    
    /**
     * Check if booking can be edited (only PENDING bookings)
     */
    public boolean canEditBooking(Booking booking) {
        if (booking.getBookingDetails() == null) {
            return false;
        }
        return "PENDING".equals(booking.getBookingDetails().getBookingStatus());
    }
    
    /**
     * Update booking details (only for PENDING bookings)
     */
    // UPDATE - Update booking
    @Transactional
    public Booking updateBooking(Long bookingId, String fullName, String phoneNumber,
                                  String emailAddress, String hall, LocalDate bookingDate,
                                  String foodType, int peopleCount, String paymentType) {
        
        Booking booking = getBookingById(bookingId);
        
        // Only allow editing if booking is still PENDING
        if (!canEditBooking(booking)) {
            throw new RuntimeException("Cannot edit booking. Booking has already been confirmed or processed.");
        }
        
        if (peopleCount > 800) {
            throw new RuntimeException("Maximum 800 people allowed");
        }
        
        // Check if hall is available on new date (excluding current booking and only checking confirmed bookings)
        if (!booking.getHall().equals(hall) || !booking.getBookingDate().equals(bookingDate)) {
            List<Booking> conflictingBookings = bookingRepository.findAll().stream()
                    .filter(b -> !b.getId().equals(bookingId)) // Exclude current booking
                    .filter(b -> hall.equals(b.getHall()) && bookingDate.equals(b.getBookingDate()))
                    .filter(b -> {
                        // Only check confirmed bookings (ACCEPTED or BOOKED)
                        if (b.getBookingDetails() != null) {
                            String status = b.getBookingDetails().getBookingStatus();
                            return "ACCEPTED".equals(status) || "BOOKED".equals(status);
                        }
                        return false;
                    })
                    .toList();
            
            if (!conflictingBookings.isEmpty()) {
                throw new RuntimeException("Hall is already booked for this date");
            }
        }
        
        // Update booking
        booking.setFullName(fullName);
        booking.setPhoneNumber(phoneNumber);
        booking.setEmailAddress(emailAddress);
        booking.setHall(hall);
        booking.setBookingDate(bookingDate);
        
        // Update booking details
        BookingDetails details = booking.getBookingDetails();
        if (details == null) {
            details = bookingDetailsRepository.findByBookingId(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking details not found"));
        }
        
        double totalAmount = calculateTotalAmount(foodType, peopleCount);
        details.setFoodType(foodType);
        details.setPeopleCount(peopleCount);
        details.setPaymentType(paymentType);
        details.setTotalAmount(totalAmount);
        
        bookingDetailsRepository.save(details);
        return bookingRepository.save(booking);
    }
    
    /**
     * Cancel/Delete booking (only for PENDING bookings)
     */
    // DELETE - Cancel booking
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        
        // Only allow cancellation if booking is still PENDING
        if (!canEditBooking(booking)) {
            throw new RuntimeException("Cannot cancel booking. Booking has already been confirmed or processed.");
        }
        
        // Update admin table - decrement booking count
        User user = booking.getUser();
        Optional<Admin> adminOptional = adminRepository.findByUserId(user.getId());
        adminOptional.ifPresent(admin -> {
            admin.setTotalBookings(Math.max(0, admin.getTotalBookings() - 1));
            adminRepository.save(admin);
        });
        
        bookingRepository.delete(booking);
    }
    
    /**
     * Get available dates for a specific hall
     * Returns list of booked dates to exclude from calendar
     * Only includes ACCEPTED and BOOKED status (not PENDING)
     */
    // READ - Get booked dates for calendar
    public List<LocalDate> getBookedDatesForHall(String hall) {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .filter(b -> hall.equals(b.getHall()))
                .filter(b -> {
                    // Only show as booked if admin has confirmed (ACCEPTED or BOOKED status)
                    if (b.getBookingDetails() != null) {
                        String status = b.getBookingDetails().getBookingStatus();
                        return "ACCEPTED".equals(status) || "BOOKED".equals(status);
                    }
                    return false;
                })
                .map(Booking::getBookingDate)
                .toList();
    }
    
    /**
     * Check if a specific hall and date combination is available
     * Only considers ACCEPTED and BOOKED bookings as conflicts
     */
    public boolean isDateAvailable(String hall, LocalDate date) {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .filter(b -> hall.equals(b.getHall()) && date.equals(b.getBookingDate()))
                .noneMatch(b -> {
                    if (b.getBookingDetails() != null) {
                        String status = b.getBookingDetails().getBookingStatus();
                        return "ACCEPTED".equals(status) || "BOOKED".equals(status);
                    }
                    return false;
                });
    }
    
    /**
     * Get booking with details for viewing
     */
    // READ - Get booking with details
    public Booking getBookingWithDetails(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        // Eager load booking details
        if (booking.getBookingDetails() == null) {
            BookingDetails details = bookingDetailsRepository.findByBookingId(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking details not found"));
            booking.setBookingDetails(details);
        }
        return booking;
    }
}

