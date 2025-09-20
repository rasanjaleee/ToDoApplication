package com.example.taskmanager.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginUITest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        String ciEnv = System.getenv("CI");
        if ("true".equals(ciEnv)) {
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
    }

    @Test
    void testSuccessfulLogin() {
        driver.get("http://localhost:5173/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email"))).sendKeys("test@example.com");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Use text-based check instead of ID
        WebElement welcome = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Welcome')]")
        ));
        Assertions.assertTrue(welcome.getText().contains("Welcome"));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
