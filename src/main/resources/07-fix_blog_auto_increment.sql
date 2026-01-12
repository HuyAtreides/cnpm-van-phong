-- Fix blog table to add AUTO_INCREMENT to blog_id
USE cnpm;

-- Modify blog_id to have AUTO_INCREMENT
ALTER TABLE blog MODIFY COLUMN blog_id INT AUTO_INCREMENT;

-- Verify the change
DESCRIBE blog;
