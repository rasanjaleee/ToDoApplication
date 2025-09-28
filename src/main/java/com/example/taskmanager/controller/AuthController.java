package com.example.taskmanager.controller;

import com.example.taskmanager.dto.LoginRequest;
import com.example.taskmanager.dto.SignupRequest;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        try {
            if (!signUpRequest.getEmail().contains("@") || !signUpRequest.getEmail().contains(".")) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid email format!"));
            }

            if (userService.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Email is already in use!"));
            }

            User user = userService.registerUser(signUpRequest);
            return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.loginUser(loginRequest);
            return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // Test connection endpoint
    @GetMapping("/test")
    public ResponseEntity<?> testConnection() {
        return ResponseEntity.ok(new MessageResponse("Backend is connected!"));
    }
    static class UserResponse {
        private Long userId;
        private String name;
        private String email;

        public UserResponse(Long userId, String name, String email) {
            this.userId = userId;
            this.name = name;
            this.email = email;
        }

        public Long getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }

    static class MessageResponse {
        private String message;
        public MessageResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}