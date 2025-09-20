package com.example.taskmanager.controller;
import com.example.taskmanager.dto.*;
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        try {
            User user = userService.registerUser(signUpRequest);
            AuthResponse response = new AuthResponse();
            response.setUserId(user.getId());
            response.setEmail(user.getEmail());
            response.setName(user.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.loginUser(loginRequest);
            AuthResponse response = new AuthResponse();
            response.setUserId(user.getId());
            response.setEmail(user.getEmail());
            response.setName(user.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testConnection() {
        return ResponseEntity.ok("Backend is connected!");
    }
}