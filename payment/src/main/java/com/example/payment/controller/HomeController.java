package com.example.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login.html";
    }
    
    @GetMapping("/index")
    public String index() {
        return "redirect:/login.html";
    }
    
    @GetMapping("/home")
    public String homePage() {
        return "redirect:/login.html";
    }
}
