package com.example.taskmanager.unit;

import com.example.taskmanager.controller.AuthController;
import com.example.taskmanager.dto.LoginRequest;
import com.example.taskmanager.dto.SignupRequest;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userService.loginUser(any(LoginRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));

        verify(userService, times(1)).loginUser(any(LoginRequest.class));
    }

    @Test
    void testLoginFailure() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        when(userService.loginUser(any(LoginRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid password"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid password"));

        verify(userService, times(1)).loginUser(any(LoginRequest.class));
    }

    @Test
    void testSignupSuccess() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("New User");
        signupRequest.setEmail("new@example.com");
        signupRequest.setPassword("password123");

        String signupJson = objectMapper.writeValueAsString(signupRequest);

        User newUser = new User();
        newUser.setId(2L);
        newUser.setName("New User");
        newUser.setEmail("new@example.com");
        newUser.setPassword("encodedPassword");

        when(userService.registerUser(any(SignupRequest.class))).thenReturn(newUser);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.name").value("New User"));

        verify(userService, times(1)).registerUser(any(SignupRequest.class));
    }

    @Test
    void testSignupFailure() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("Test User");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");

        String signupJson = objectMapper.writeValueAsString(signupRequest);

        when(userService.registerUser(any(SignupRequest.class)))
                .thenThrow(new IllegalArgumentException("Email is already in use!"));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email is already in use!"));

        verify(userService, times(1)).registerUser(any(SignupRequest.class));
    }

    @Test
    void testConnection() throws Exception {
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Backend is connected!"));
    }
}