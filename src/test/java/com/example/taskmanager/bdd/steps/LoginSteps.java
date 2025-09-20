package com.example.taskmanager.bdd.steps;

import com.example.taskmanager.dto.LoginRequest;
import com.example.taskmanager.dto.SignupRequest;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.UserService;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {

    @Autowired
    private UserService userService;

    private LoginRequest loginRequest;
    private User loggedInUser;
    private Exception loginException;

    @Given("I have a registered user with email {string} and password {string}")
    public void i_have_a_registered_user_with_email_and_password(String email, String password) {
        // First create the user if it doesn't exist
        try {
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setName("Test User");
            signupRequest.setEmail(email);
            signupRequest.setPassword(password);
            userService.registerUser(signupRequest);
        } catch (Exception e) {
            // User might already exist, which is fine
            System.out.println("User might already exist: " + e.getMessage());
        }

        // Then set up login request
        loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
    }

    @Given("I have a user with email {string} and password {string}")
    public void i_have_a_user_with_email_and_password(String email, String password) {
        loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
    }

    @When("I send a login request")
    public void i_send_a_login_request() {
        try {
            loggedInUser = userService.loginUser(loginRequest);
            System.out.println("Login successful for: " + loggedInUser.getEmail());
        } catch (IllegalArgumentException e) {
            loginException = e;
            System.out.println("Login failed: " + e.getMessage());
        } catch (Exception e) {
            loginException = e;
            System.out.println("Unexpected login error: " + e.getMessage());
        }
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        assertNotNull(loggedInUser, "User should be logged in successfully");
        assertNull(loginException, "No exception should be thrown during login");
        assertEquals(loginRequest.getEmail(), loggedInUser.getEmail());
        assertNotNull(loggedInUser.getId(), "Logged in user should have an ID");
    }

    @Then("I should get a login error {string}")
    public void i_should_get_a_login_error(String errorMessage) {
        assertNotNull(loginException, "An exception should have been thrown during login");
        assertTrue(loginException.getMessage().contains(errorMessage),
                "Error message should contain: " + errorMessage + ". Actual: " + loginException.getMessage());
    }
}