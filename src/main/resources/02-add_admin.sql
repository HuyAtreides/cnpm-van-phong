-- Ensure ADMINISTRATOR role exists (ID 1 usually, or auto)
INSERT INTO role (role_id, role_name) VALUES (1, 'ADMINISTRATOR') ON DUPLICATE KEY UPDATE role_name='ADMINISTRATOR';

-- Create Admin User
-- Password is '123456' encoded with {noop} for simplicity or bcrypt if needed. 
-- Using {noop}123456 for immediate access as configured in PasswordEncoderConfig
INSERT INTO user (user_id, email, password, name, phone, address, gender, status, is_active, is_delete, role_id)
VALUES (
    999, 
    'admin@ergoffice.com', 
    '{noop}123456', 
    'Administrator', 
    '0909090909', 
    'Head Office', 
    'Male', 
    'Active', 
    1, 
    0, 
    1
) ON DUPLICATE KEY UPDATE password='{noop}123456', role_id=1, is_active=1;
