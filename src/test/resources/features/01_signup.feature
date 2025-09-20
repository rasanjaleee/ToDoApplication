Feature: User Signup
  As a new user
  I want to register for an account
  So that I can use the task management system

  Scenario: Successful user signup
    Given I have a new user with name "Alice", email "alice@example.com", and password "password123"
    When I send a signup request
    Then the user should be created successfully

  Scenario: Signup with existing email
    Given I have a new user with name "Bob", email "alice@example.com", and password "password123"
    When I send a signup request
    Then I should get an error that email is already in use