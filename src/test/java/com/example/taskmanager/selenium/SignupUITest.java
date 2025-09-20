package com.example.taskmanager.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignupUITest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    void testSuccessfulSignup() {
        driver.get("http://localhost:5173/signup");

        String timestamp = String.valueOf(System.currentTimeMillis());
        String email = "test" + timestamp + "@example.com";

        driver.findElement(By.id("name")).sendKeys("Test User");
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("confirmPassword")).sendKeys("password123");
        driver.findElement(By.id("signup-btn")).click();

        WebElement success = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("success-message"))
        );
        Assertions.assertTrue(success.getText().contains("Registration successful"));
    }

    @Test
    void testSignupWithExistingEmail() {
        driver.get("http://localhost:5173/signup");

        driver.findElement(By.id("name")).sendKeys("Test User");
        driver.findElement(By.id("email")).sendKeys("test@example.com"); // existing email
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("confirmPassword")).sendKeys("password123");
        driver.findElement(By.id("signup-btn")).click();

        WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("error-message"))
        );
        Assertions.assertTrue(error.getText().contains("Email already exists"));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
