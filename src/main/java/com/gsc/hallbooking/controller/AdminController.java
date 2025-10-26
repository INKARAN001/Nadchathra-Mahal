package com.gsc.hallbooking.controller;

import com.gsc.hallbooking.service.BookingService;
import com.gsc.hallbooking.service.FeedbackService;
import com.gsc.hallbooking.service.MessageService;
import com.gsc.hallbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * DEVELOPER: INKARAN
 * CRUD: Admin Dashboard & Management System
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final BookingService bookingService;
    private final FeedbackService feedbackService;
    private final UserService userService;
    private final MessageService messageService;
    
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
    
    // ========== MESSAGING SYSTEM ==========
    
    @GetMapping("/messages")
    public String messages(Model model) {
        model.addAttribute("users", messageService.getAllUsers());
        model.addAttribute("messages", messageService.getAllMessages());
        return "admin/messages";
    }
    
    @GetMapping("/messages/send")
    public String sendMessageForm(Model model) {
        model.addAttribute("users", messageService.getAllUsers());
        return "admin/send-message";
    }
    
    @PostMapping("/messages/send-broadcast")
    public String sendBroadcastMessage(@RequestParam String subject,
                                    @RequestParam String content,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        try {
            String adminEmail = authentication.getName();
            com.gsc.hallbooking.entity.User admin = userService.getCurrentUser(adminEmail);
            messageService.sendBroadcastMessage(admin, subject, content);
            redirectAttributes.addFlashAttribute("success", "Broadcast message sent to all users!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/messages";
    }
    
    @PostMapping("/messages/send-direct")
    public String sendDirectMessage(@RequestParam Long recipientId,
                                 @RequestParam String subject,
                                 @RequestParam String content,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            String adminEmail = authentication.getName();
            com.gsc.hallbooking.entity.User admin = userService.getCurrentUser(adminEmail);
            messageService.sendDirectMessage(admin, recipientId, subject, content);
            redirectAttributes.addFlashAttribute("success", "Direct message sent successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/messages";
    }
    
    // READ - View specific message
    @GetMapping("/messages/view/{id}")
    public String viewMessage(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("message", messageService.getMessageById(id));
            return "admin/view-message";
        } catch (RuntimeException e) {
            return "redirect:/admin/messages?error=" + e.getMessage();
        }
    }
    
    // UPDATE - Edit message form
    @GetMapping("/messages/edit/{id}")
    public String editMessageForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("message", messageService.getMessageById(id));
            model.addAttribute("users", messageService.getAllUsers());
            return "admin/edit-message";
        } catch (RuntimeException e) {
            return "redirect:/admin/messages?error=" + e.getMessage();
        }
    }
    
    // UPDATE - Update message
    @PostMapping("/messages/update/{id}")
    public String updateMessage(@PathVariable Long id,
                              @RequestParam String subject,
                              @RequestParam String content,
                              RedirectAttributes redirectAttributes) {
        try {
            messageService.updateMessage(id, subject, content);
            redirectAttributes.addFlashAttribute("success", "Message updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/messages";
    }
    
    // DELETE - Delete message
    @PostMapping("/messages/delete/{id}")
    public String deleteMessage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            messageService.deleteMessage(id);
            redirectAttributes.addFlashAttribute("success", "Message deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/messages";
    }
    
    // READ - Filter messages by type
    @GetMapping("/messages/filter")
    public String filterMessages(@RequestParam String type, Model model) {
        model.addAttribute("users", messageService.getAllUsers());
        model.addAttribute("messages", messageService.getMessagesByType(type));
        model.addAttribute("filterType", type);
        return "admin/messages";
    }
}

