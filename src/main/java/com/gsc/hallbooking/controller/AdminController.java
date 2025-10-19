package com.gsc.hallbooking.controller;

import com.gsc.hallbooking.service.BookingService;
import com.gsc.hallbooking.service.FeedbackService;
import com.gsc.hallbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final BookingService bookingService;
    private final FeedbackService feedbackService;
    private final UserService userService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/dashboard";
    }
    
    @PostMapping("/booking/accept/{id}")
    public String acceptBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.acceptBooking(id);
        redirectAttributes.addFlashAttribute("success", "Booking accepted successfully!");
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/booking/reject/{id}")
    public String rejectBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.rejectBooking(id);
        redirectAttributes.addFlashAttribute("success", "Booking rejected successfully!");
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/booking/mark-paid/{id}")
    public String markAsPaid(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.markPaymentAsPaid(id);
        redirectAttributes.addFlashAttribute("success", "Payment marked as paid!");
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/feedbacks")
    public String feedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "admin/feedbacks";
    }
    
    @PostMapping("/feedback/approve/{id}")
    public String approveFeedback(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        feedbackService.approveFeedback(id);
        redirectAttributes.addFlashAttribute("success", "Feedback approved!");
        return "redirect:/admin/feedbacks";
    }
    
    @PostMapping("/feedback/disapprove/{id}")
    public String disapproveFeedback(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        feedbackService.disapproveFeedback(id);
        redirectAttributes.addFlashAttribute("success", "Feedback disapproved!");
        return "redirect:/admin/feedbacks";
    }
    
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<com.gsc.hallbooking.entity.User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        
        // Calculate statistics
        long activeUsers = users.stream().filter(com.gsc.hallbooking.entity.User::getEnabled).count();
        long disabledUsers = users.stream().filter(u -> !u.getEnabled()).count();
        long adminUsers = users.stream().filter(u -> "ADMIN".equals(u.getRole())).count();
        
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("disabledUsers", disabledUsers);
        model.addAttribute("adminUsers", adminUsers);
        
        return "admin/users";
    }
    
    @PostMapping("/user/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserStatus(id);
            redirectAttributes.addFlashAttribute("success", "User status updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
    
    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}

