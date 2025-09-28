package com.example.payment.controller;

import com.example.payment.entity.User;
import com.example.payment.respository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Validated
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Optional<User> userOpt = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                if (!user.getIsActive()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "ERROR");
                    response.put("message", "Account is deactivated");
                    return ResponseEntity.badRequest().body(response);
                }
                
                Map<String, Object> response = new HashMap<>();
                response.put("status", "SUCCESS");
                response.put("message", "Login successful");
                response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "userType", user.getUserType()
                ));
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "ERROR");
                response.put("message", "Invalid credentials");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ERROR");
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Check if username already exists
            if (userRepository.existsByUsername(request.getUsername())) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "ERROR");
                response.put("message", "Username already exists");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Check if email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "ERROR");
                response.put("message", "Email already exists");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Create new user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            user.setPhone(request.getPhone());
            user.setUserType("USER"); // Default to USER type
            
            User savedUser = userRepository.save(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "User registered successfully");
            response.put("user", Map.of(
                "id", savedUser.getId(),
                "username", savedUser.getUsername(),
                "email", savedUser.getEmail(),
                "fullName", savedUser.getFullName(),
                "userType", savedUser.getUserType()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ERROR");
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, String> signupRequest) {
        try {
            String fullName = signupRequest.get("fullName");
            String username = signupRequest.get("username");
            String email = signupRequest.get("email");
            String phone = signupRequest.get("phone");
            String password = signupRequest.get("password");
            String confirmPassword = signupRequest.get("confirmPassword");

            // Validation
            if (fullName == null || fullName.trim().isEmpty()) {
                return createErrorResponse("Full name is required");
            }
            if (username == null || username.trim().isEmpty()) {
                return createErrorResponse("Username is required");
            }
            if (email == null || email.trim().isEmpty()) {
                return createErrorResponse("Email is required");
            }
            if (phone == null || phone.trim().isEmpty()) {
                return createErrorResponse("Phone number is required");
            }
            if (password == null || password.trim().isEmpty()) {
                return createErrorResponse("Password is required");
            }
            if (confirmPassword == null || !password.equals(confirmPassword)) {
                return createErrorResponse("Passwords do not match");
            }

            // Check if username already exists
            if (userRepository.existsByUsername(username)) {
                return createErrorResponse("Username already exists");
            }

            // Check if email already exists
            if (userRepository.existsByEmail(email)) {
                return createErrorResponse("Email already exists");
            }

            // Create new user
            User newUser = new User();
            newUser.setFullName(fullName.trim());
            newUser.setUsername(username.trim());
            newUser.setEmail(email.trim());
            newUser.setPhone(phone.trim());
            newUser.setPassword(password);
            newUser.setUserType("USER"); // All signups are regular users
            newUser.setIsActive(true);

            User savedUser = userRepository.save(newUser);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Account created successfully");
            response.put("user", Map.of(
                "id", savedUser.getId(),
                "username", savedUser.getUsername(),
                "email", savedUser.getEmail(),
                "fullName", savedUser.getFullName(),
                "userType", savedUser.getUserType()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Signup failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "ERROR");
        errorResponse.put("message", message);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Inner classes for request DTOs
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;
        private String fullName;
        private String phone;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
