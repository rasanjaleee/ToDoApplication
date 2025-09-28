package com.example.taskmanager.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupUITest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeEach
    void setup() {
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        String ciEnv = System.getenv("CI");
        if ("true".equals(ciEnv)) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
        baseUrl = "http://localhost:5173";
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
    }

    @Test
    public void testSuccessfulSignup() {
        driver.get(baseUrl + "/signup");

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        WebElement confirmPasswordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmPassword"))); // ✅ Added
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("signup-btn")));

        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";

        nameInput.sendKeys("Test User");
        emailInput.sendKeys(uniqueEmail);
        passwordInput.sendKeys("password123");
        confirmPasswordInput.sendKeys("password123"); // ✅ Added
        submitButton.click();

        try {

            wait.until(ExpectedConditions.urlContains("login"));
            assertTrue(driver.getCurrentUrl().contains("login"), "Should be redirected to login page");
        } catch (TimeoutException e) {
            // fallback: check success message
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message")));
            assertTrue(successMessage.isDisplayed(), "Success message should be displayed");
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
