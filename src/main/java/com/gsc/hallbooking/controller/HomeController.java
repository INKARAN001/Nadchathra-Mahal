package com.gsc.hallbooking.controller;

import com.gsc.hallbooking.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final FeedbackService feedbackService;
    
    @GetMapping("/")
    public String home(Model model) {
        try {
            model.addAttribute("feedbacks", feedbackService.getApprovedFeedbacks());
        } catch (Exception e) {
            // If there's an error getting feedbacks, just show empty list
            model.addAttribute("feedbacks", Collections.emptyList());
        }
        return "home";
    }
    
    @GetMapping("/halls")
    public String halls() {
        return "halls";
    }
    
    @GetMapping("/food-menu")
    public String foodMenu() {
        return "food-menu";
    }
    
    @GetMapping("/events")
    public String events() {
        return "events";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}

