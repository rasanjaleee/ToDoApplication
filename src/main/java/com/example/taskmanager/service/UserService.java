package com.example.taskmanager.service;

import com.example.taskmanager.model.User;
import com.example.taskmanager.dto.SignupRequest;
import com.example.taskmanager.dto.LoginRequest;

public interface UserService {
    User registerUser(SignupRequest signUpRequest);
    User loginUser(LoginRequest loginRequest);
    boolean existsByEmail(String email);
}