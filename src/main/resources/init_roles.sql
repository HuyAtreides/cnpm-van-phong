-- Script để tạo các Role ban đầu
-- Chạy script này trước khi đăng ký user

USE cnpm;

-- Xóa roles cũ (nếu có)
DELETE FROM role;

-- Thêm 3 roles cơ bản
INSERT INTO role (roleId, roleName) VALUES 
(1, 'ADMINISTRATOR'),
(2, 'EMPLOYEE'),
(3, 'CUSTOMER');

-- Kiểm tra kết quả
SELECT * FROM role;
