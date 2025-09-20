package com.example.taskmanager.bdd.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration // ADD THIS ANNOTATION
@ActiveProfiles("test")
public class CucumberSpringConfiguration {
    // This class can be empty
}