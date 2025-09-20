package com.example.taskmanager.bdd.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.example.taskmanager.bdd.steps",
                "com.example.taskmanager.bdd.config" // Make sure this includes config package
        },
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        monochrome = true
)
public class CucumberTestRunner {
}