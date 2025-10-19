package com.gsc.hallbooking.config;

import com.gsc.hallbooking.entity.User;
import com.gsc.hallbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        try {
            // Create default admin user if not exists
            if (!userRepository.existsByEmail("admin@gsc.com")) {
                User admin = User.builder()
                        .name("Admin")
                        .email("admin@gsc.com")
                        .password(passwordEncoder.encode("admin123"))
                        .phone("1234567890")
                        .role("ADMIN")
                        .enabled(true)
                        .build();
                userRepository.save(admin);
                System.out.println("Default admin user created: admin@gsc.com / admin123");
            }
        } catch (Exception e) {
            System.err.println("Error initializing default admin user: " + e.getMessage());
            // Don't fail the application startup if admin user creation fails
        }
    }
}

