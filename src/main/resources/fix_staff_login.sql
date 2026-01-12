-- Script sửa lỗi đăng nhập Staff
USE cnpm;

-- Tắt foreign key checks tạm thời
SET FOREIGN_KEY_CHECKS = 0;

-- Xóa các staff user cũ nếu có (TRƯỚC KHI XÓA ROLE)
DELETE FROM user WHERE email IN ('staff@ergoffice.com', 'nhanvien@ergoffice.com');

-- Xóa và tạo lại các role đúng
DELETE FROM role WHERE role_id IN (2, 3, 4);

INSERT INTO role (role_id, role_name) VALUES 
(1, 'ADMINISTRATOR'),
(3, 'CUSTOMER'),
(4, 'STAFF');

-- Tạo tài khoản staff với mật khẩu đúng
-- Password: 123456 (plain text với {noop} prefix)
INSERT INTO user (user_id, name, email, password, phone, address, gender, status, is_active, is_delete, role_id) VALUES
(9001, 'Staff User', 'staff@ergoffice.com', '{noop}123456', '0123456789', 'Office', 'Other', 'Active', 1, 0, 4),
(9002, 'Nhan Vien', 'nhanvien@ergoffice.com', '{noop}123123', '0987654321', 'Office 2', 'Male', 'Active', 1, 0, 4);

-- Thêm vào bảng staff (nếu có bảng riêng)
INSERT INTO staff (user_id, position, department) VALUES
(9001, 'Manager', 'Sales'),
(9002, 'Staff', 'Support');

-- Bật lại foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Kiểm tra kết quả
SELECT u.user_id, u.name, u.email, u.password, r.role_name 
FROM user u 
JOIN role r ON u.role_id = r.role_id 
WHERE u.email IN ('staff@ergoffice.com', 'nhanvien@ergoffice.com');
