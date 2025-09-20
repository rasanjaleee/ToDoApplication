Feature: User Login
  As a registered user
  I want to login to my account
  So that I can access my tasks

  Background:
    Given I have a registered user with email "alice@example.com" and password "password123"

  Scenario: Successful login
    Given I have a user with email "alice@example.com" and password "password123"
    When I send a login request
    Then I should be logged in successfully

  Scenario: Login with wrong password
    Given I have a user with email "alice@example.com" and password "wrongpassword"
    When I send a login request
    Then I should get a login error "Invalid password"