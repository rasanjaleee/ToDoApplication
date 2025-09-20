-- Clear existing data
DELETE FROM users;

-- Insert test users
INSERT INTO users (name, email, password) VALUES
('Test User', 'test@example.com', '$2a$10$ExampleEncodedPasswordHash'),
('Admin User', 'admin@example.com', '$2a$10$AnotherExamplePasswordHash');