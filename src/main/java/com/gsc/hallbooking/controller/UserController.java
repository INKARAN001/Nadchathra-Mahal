package com.gsc.hallbooking.controller;

import com.gsc.hallbooking.entity.Booking;
import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.service.BookingService;
import com.gsc.hallbooking.service.FeedbackService;
import com.gsc.hallbooking.service.PaymentService;
import com.gsc.hallbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final BookingService bookingService;
    private final FeedbackService feedbackService;
    private final PaymentService paymentService;
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.getCurrentUser(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingService.getUserBookings(user));
        return "user/dashboard";
    }
    
    @GetMapping("/booking/stage1")
    public String bookingStage1() {
        return "user/booking-stage1";
    }
    
    @PostMapping("/booking/stage1")
    public String processStage1(@RequestParam String fullName,
                               @RequestParam String phoneNumber,
                               @RequestParam String emailAddress,
                               Model model) {
        model.addAttribute("fullName", fullName);
        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("emailAddress", emailAddress);
        return "redirect:/user/booking/stage2";
    }
    
    @GetMapping("/booking/stage2")
    public String bookingStage2() {
        return "user/booking-stage2";
    }
    
    @PostMapping("/booking/stage2")
    public String processStage2(@RequestParam String hall,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate,
                               Model model) {
        model.addAttribute("hall", hall);
        model.addAttribute("bookingDate", bookingDate);
        model.addAttribute("foodPrices", bookingService.getFoodPrices());
        return "redirect:/user/booking/stage3";
    }
    
    @GetMapping("/booking/stage3")
    public String bookingStage3(Model model) {
        model.addAttribute("foodPrices", bookingService.getFoodPrices());
        return "user/booking-stage3";
    }
    
    @PostMapping("/booking/submit")
    public String submitBooking(@RequestParam String fullName,
                               @RequestParam String phoneNumber,
                               @RequestParam String emailAddress,
                               @RequestParam String hall,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate,
                               @RequestParam String foodType,
                               @RequestParam int peopleCount,
                               @RequestParam String paymentType,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getCurrentUser(authentication.getName());
            bookingService.createBooking(user, fullName, phoneNumber, emailAddress, 
                                        hall, bookingDate, foodType, peopleCount, paymentType);
            redirectAttributes.addFlashAttribute("success", "Booking request submitted successfully!");
            return "redirect:/user/dashboard";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/booking/stage1";
        }
    }
    
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
            if (!"ACCEPTED".equals(booking.getBookingStatus()) || !"CARD".equals(booking.getPaymentType())) {
                return "redirect:/user/dashboard";
            }
            
            model.addAttribute("booking", booking);
            return "user/payment-gateway";
        } catch (RuntimeException e) {
            return "redirect:/user/dashboard";
        }
    }
    
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
            
            // Create payment record
            paymentService.createPayment(booking, "CARD", transactionId);
            
            // Mark booking payment as paid
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
}

