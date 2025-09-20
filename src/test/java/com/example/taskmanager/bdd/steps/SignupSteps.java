package com.example.taskmanager.bdd.steps;

import com.example.taskmanager.dto.SignupRequest;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.UserService;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

public class SignupSteps {

    @Autowired
    private UserService userService;

    private SignupRequest signupRequest;
    private User responseUser;
    private Exception exception;

    @Given("I have a new user with name {string}, email {string}, and password {string}")
    public void i_have_a_new_user(String name, String email, String password) {
        signupRequest = new SignupRequest();
        signupRequest.setName(name);
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);
    }

    @When("I send a signup request")
    public void i_send_a_signup_request() {
        try {
            responseUser = userService.registerUser(signupRequest);
            System.out.println("User registered successfully: " + responseUser.getEmail());
        } catch (IllegalArgumentException e) {
            exception = e;
            System.out.println("Validation error: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            exception = e;
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            exception = e;
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Then("the user should be created successfully")
    public void the_user_should_be_created_successfully() {
        assertNotNull(responseUser, "User service should return a non-null response");
        assertNull(exception, "No exception should be thrown. Exception: " +
                (exception != null ? exception.getMessage() : "null"));
        assertEquals(signupRequest.getEmail(), responseUser.getEmail());
        assertNotNull(responseUser.getId(), "User should have an ID after creation");
        assertNotNull(responseUser.getName(), "User should have a name");
    }

    @Then("I should get an error that email is already in use")
    public void i_should_get_an_error_that_email_is_already_in_use() {
        assertNotNull(exception, "An exception should have been thrown");
        assertTrue(exception.getMessage().contains("Email is already in use"),
                "Error message should contain 'Email is already in use'. Actual: " + exception.getMessage());
    }

    @Then("I should get a signup error {string}")
    public void i_should_get_a_signup_error(String errorMessage) {
        assertNotNull(exception, "An exception should have been thrown");
        assertTrue(exception.getMessage().contains(errorMessage),
                "Error message should contain: " + errorMessage + ". Actual: " + exception.getMessage());
    }
}