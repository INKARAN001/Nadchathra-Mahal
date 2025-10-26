package com.gsc.hallbooking.controller;

import com.gsc.hallbooking.entity.Booking;
import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.service.BookingService;
import com.gsc.hallbooking.service.FeedbackService;
import com.gsc.hallbooking.service.MessageService;
import com.gsc.hallbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final BookingService bookingService;
    private final FeedbackService feedbackService;
    // Payment functionality is handled in booking_details
    private final MessageService messageService;
    
    // DEVELOPER: AKASH - User Dashboard & Booking System
    
    // READ - User dashboard
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.getCurrentUser(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingService.getUserBookings(user));
        model.addAttribute("messages", messageService.getMessagesForUser(user));
        model.addAttribute("broadcastMessages", messageService.getBroadcastMessages());
        model.addAttribute("unreadCount", messageService.getUnreadMessageCountForUser(user));
        return "user/dashboard";
    }
    
    // CREATE - Booking stage 1 (hall/date selection)
    @GetMapping("/booking/stage1")
    public String bookingStage1(Authentication authentication, Model model) {
        User user = userService.getCurrentUser(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("halls", bookingService.getActiveHalls());
        return "user/booking-stage1";
    }
    
    // CREATE - Process booking stage 1
    @PostMapping("/booking/stage1")
    public String processStage1(@RequestParam String hall,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate,
                               Authentication authentication,
                               Model model) {
        User user = userService.getCurrentUser(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("hall", hall);
        model.addAttribute("bookingDate", bookingDate);
        model.addAttribute("foodPrices", bookingService.getFoodPrices());
        return "redirect:/user/booking/stage2?hall=" + hall + "&bookingDate=" + bookingDate;
    }
    
    // CREATE - Booking stage 2 (food & payment selection)
    @GetMapping("/booking/stage2")
    public String bookingStage2(@RequestParam String hall,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate,
                               Authentication authentication,
                               Model model) {
        User user = userService.getCurrentUser(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("hall", hall);
        model.addAttribute("bookingDate", bookingDate);
        model.addAttribute("foodItems", bookingService.getActiveFoodItems());
        return "user/booking-stage2";
    }
    
    // CREATE - Submit booking
    @PostMapping("/booking/submit")
    public String submitBooking(@RequestParam String hall,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate,
                               @RequestParam String foodType,
                               @RequestParam int peopleCount,
                               @RequestParam String paymentType,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getCurrentUser(authentication.getName());
            // Use logged-in user's information
            bookingService.createBooking(user, user.getName(), user.getPhone(), user.getEmail(), 
                                        hall, bookingDate, foodType, peopleCount, paymentType);
            redirectAttributes.addFlashAttribute("success", "Booking request submitted successfully!");
            return "redirect:/user/dashboard";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/booking/stage1";
        }
    }
    
    // READ - Payment gateway
    @GetMapping("/booking/payment-gateway/{id}")
    public String paymentGateway(@PathVariable Long id, Model model, Authentication authentication) {
        try {
            Booking booking = bookingService.getBookingById(id);
            User user = userService.getCurrentUser(authentication.getName());
            
            // Verify booking belongs to user
            if (!booking.getUser().getId().equals(user.getId())) {
                return "redirect:/user/dashboard";
            }
            
            // Only allow payment for ACCEPTED bookings with CARD payment type
            if (booking.getBookingDetails() == null ||
                !"ACCEPTED".equals(booking.getBookingDetails().getBookingStatus()) || 
                !"CARD".equals(booking.getBookingDetails().getPaymentType())) {
                return "redirect:/user/dashboard";
            }
            
            model.addAttribute("booking", booking);
            return "user/payment-gateway";
        } catch (RuntimeException e) {
            return "redirect:/user/dashboard";
        }
    }
    
    // UPDATE - Process card payment
    @PostMapping("/booking/process-payment/{id}")
    public String processCardPayment(@PathVariable Long id,
                                    @RequestParam String cardNumber,
                                    @RequestParam String cardName,
                                    @RequestParam String expiryDate,
                                    @RequestParam String cvv,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        try {
            Booking booking = bookingService.getBookingById(id);
            User user = userService.getCurrentUser(authentication.getName());
            
            // Verify booking belongs to user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access!");
                return "redirect:/user/dashboard";
            }
            
            // Simulate payment validation
            if (cardNumber.length() < 16 || cvv.length() != 3) {
                redirectAttributes.addFlashAttribute("error", "Invalid card details!");
                return "redirect:/user/booking/payment-gateway/" + id;
            }
            
            // Simulate payment processing delay
            Thread.sleep(1000);
            
            // Generate transaction ID
            String transactionId = "TXN" + System.currentTimeMillis();
            
            // Mark booking payment as paid (payment details are stored in booking_details table)
            bookingService.markPaymentAsPaid(id);
            
            redirectAttributes.addFlashAttribute("success", "Payment successful! Your booking is confirmed. Transaction ID: " + transactionId);
            return "redirect:/user/dashboard";
        } catch (InterruptedException e) {
            redirectAttributes.addFlashAttribute("error", "Payment processing failed. Please try again.");
            return "redirect:/user/booking/payment-gateway/" + id;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/dashboard";
        }
    }
    
    @GetMapping("/feedback")
    public String feedbackForm() {
        return "user/feedback";
    }
    
    @PostMapping("/feedback")
    public String submitFeedback(@RequestParam String message,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser(authentication.getName());
        feedbackService.createFeedback(user, message);
        redirectAttributes.addFlashAttribute("success", "Feedback submitted successfully!");
        return "redirect:/user/dashboard";
    }
    
    // DEVELOPER: JADAVAN - User Profile Management Endpoints
    
    // READ - View user profile
    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        User user = userService.getCurrentUser(authentication.getName());
        model.addAttribute("user", user);
        return "user/profile";
    }
    
    // READ - Edit profile form
    @GetMapping("/profile/edit")
    public String editProfile(Authentication authentication, Model model) {
        User user = userService.getCurrentUser(authentication.getName());
        model.addAttribute("user", user);
        return "user/profile-edit";
    }
    
    // UPDATE - Update user profile
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String name,
                               @RequestParam String phone,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getCurrentUser(authentication.getName());
            userService.updateUserProfile(user.getId(), name, phone);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            return "redirect:/user/profile";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/profile/edit";
        }
    }
    
    // UPDATE - Change user password
    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "New passwords do not match!");
                return "redirect:/user/profile/edit";
            }
            
            if (newPassword.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters long!");
                return "redirect:/user/profile/edit";
            }
            
            User user = userService.getCurrentUser(authentication.getName());
            userService.updateUserPassword(user.getId(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully!");
            return "redirect:/user/profile";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/profile/edit";
        }
    }
    
    // DELETE - Delete user account
    @PostMapping("/profile/delete")
    public String deleteAccount(@RequestParam String password,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getCurrentUser(authentication.getName());
            userService.deleteOwnAccount(user.getId(), password);
            redirectAttributes.addFlashAttribute("info", "Your account has been deleted successfully.");
            return "redirect:/?logout=true";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/profile";
        }
    }
    
    // DEVELOPER: AKASH - Booking CRUD Operations & Calendar
    
    // READ - View booking details
    @GetMapping("/booking/view/{id}")
    public String viewBooking(@PathVariable Long id, Authentication authentication, Model model) {
        try {
            User user = userService.getCurrentUser(authentication.getName());
            Booking booking = bookingService.getBookingWithDetails(id);
            
            // Verify booking belongs to user
            if (!booking.getUser().getId().equals(user.getId())) {
                return "redirect:/user/dashboard";
            }
            
            model.addAttribute("booking", booking);
            model.addAttribute("canEdit", bookingService.canEditBooking(booking));
            return "user/booking-view";
        } catch (RuntimeException e) {
            return "redirect:/user/dashboard";
        }
    }
    
    // READ - Edit booking form
    @GetMapping("/booking/edit/{id}")
    public String editBookingForm(@PathVariable Long id, Authentication authentication, Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getCurrentUser(authentication.getName());
            Booking booking = bookingService.getBookingWithDetails(id);
            
            // Verify booking belongs to user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access!");
                return "redirect:/user/dashboard";
            }
            
            // Check if booking can be edited
            if (!bookingService.canEditBooking(booking)) {
                redirectAttributes.addFlashAttribute("error", "This booking cannot be edited as it has already been confirmed or processed.");
                return "redirect:/user/dashboard";
            }
            
            model.addAttribute("booking", booking);
            model.addAttribute("halls", bookingService.getActiveHalls());
            model.addAttribute("foodItems", bookingService.getActiveFoodItems());
            return "user/booking-edit";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/dashboard";
        }
    }
    
    // UPDATE - Update booking
    @PostMapping("/booking/update/{id}")
    public String updateBooking(@PathVariable Long id,
                               @RequestParam String hall,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate,
                               @RequestParam String foodType,
                               @RequestParam int peopleCount,
                               @RequestParam String paymentType,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getCurrentUser(authentication.getName());
            Booking booking = bookingService.getBookingById(id);
            
            // Verify booking belongs to user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access!");
                return "redirect:/user/dashboard";
            }
            
            // Personal info comes from existing booking (not editable)
            bookingService.updateBooking(id, booking.getFullName(), booking.getPhoneNumber(), 
                                        booking.getEmailAddress(), hall, bookingDate, 
                                        foodType, peopleCount, paymentType);
            
            redirectAttributes.addFlashAttribute("success", "Booking updated successfully!");
            return "redirect:/user/dashboard";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/booking/edit/" + id;
        }
    }
    
    // DELETE - Cancel booking
    @PostMapping("/booking/cancel/{id}")
    public String cancelBooking(@PathVariable Long id,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getCurrentUser(authentication.getName());
            Booking booking = bookingService.getBookingById(id);
            
            // Verify booking belongs to user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access!");
                return "redirect:/user/dashboard";
            }
            
            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully!");
            return "redirect:/user/dashboard";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/dashboard";
        }
    }
    
    // READ - Calendar API (get booked dates)
    @GetMapping("/booking/api/booked-dates")
    @ResponseBody
    public List<String> getBookedDates(@RequestParam String hall) {
        return bookingService.getBookedDatesForHall(hall)
                .stream()
                .map(date -> date.toString())
                .toList();
    }
    
    // ========== MESSAGE SYSTEM ==========
    
    @GetMapping("/messages")
    public String viewMessages(Authentication authentication, Model model) {
        User user = userService.getCurrentUser(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("messages", messageService.getMessagesForUser(user));
        model.addAttribute("unreadCount", messageService.getUnreadMessageCountForUser(user));
        return "user/messages";
    }
    
    @PostMapping("/messages/mark-read/{id}")
    public String markMessageAsRead(@PathVariable Long id, 
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getCurrentUser(authentication.getName());
            messageService.markMessageAsRead(id, user);
            redirectAttributes.addFlashAttribute("success", "Message marked as read!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/messages";
    }
    
    @PostMapping("/messages/mark-all-read")
    public String markAllMessagesAsRead(Authentication authentication,
                                      RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getCurrentUser(authentication.getName());
            messageService.markAllMessagesAsRead(user);
            redirectAttributes.addFlashAttribute("success", "All messages marked as read!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/messages";
    }
}

