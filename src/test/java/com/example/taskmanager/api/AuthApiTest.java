package com.example.taskmanager.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        // Register parser for plain text errors
        RestAssured.registerParser("text/plain", Parser.TEXT);
    }

    @Test
    void testLoginApiSuccess() {
        String loginJson = "{\"email\": \"test@example.com\", \"password\": \"password123\"}";

        given()
                .contentType(ContentType.JSON)
                .body(loginJson)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("email", equalTo("test@example.com"))
                .body("userId", notNullValue())
                .body("name", notNullValue());
    }

    @Test
    void testLoginApiFailure() {
        String loginJson = "{\"email\": \"wrong@example.com\", \"password\": \"wrongpassword\"}";

        given()
                .contentType(ContentType.JSON)
                .body(loginJson)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(400) // AuthController returns badRequest()
                .body("message", equalTo("Invalid email or password"));
    }

    @Test
    void testSignupApiSuccess() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signupJson = "{\"name\": \"Test User\", \"email\": \"test" + timestamp + "@example.com\", \"password\": \"password123\"}";

        given()
                .contentType(ContentType.JSON)
                .body(signupJson)
                .when()
                .post("/api/auth/signup")
                .then()
                .statusCode(200) // AuthController returns ok()
                .body("email", containsString("@example.com"))
                .body("userId", notNullValue())
                .body("name", equalTo("Test User"));
    }

    @Test
    void testSignupApiWithExistingEmail() {
        String signupJson = "{\"name\": \"Test User\", \"email\": \"test@example.com\", \"password\": \"password123\"}";

        given()
                .contentType(ContentType.JSON)
                .body(signupJson)
                .when()
                .post("/api/auth/signup")
                .then()
                .statusCode(400) // badRequest
                .body("message", equalTo("Email is already in use!")); // plain text
    }
}
