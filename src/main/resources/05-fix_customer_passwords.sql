-- Fix customer passwords to work with DelegatingPasswordEncoder
USE cnpm;

-- Update all customer passwords that start with $2 (BCrypt) to have {bcrypt} prefix
UPDATE user 
SET password = CONCAT('{bcrypt}', password)
WHERE role_id = 3 
  AND password LIKE '$2%'
  AND password NOT LIKE '{bcrypt}%';

-- Verify the update
SELECT user_id, email, LEFT(password, 20) as pwd_preview, role_id 
FROM user 
WHERE role_id = 3 
LIMIT 5;
