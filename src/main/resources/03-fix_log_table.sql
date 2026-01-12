-- Script để sửa bảng log - thêm AUTO_INCREMENT cho log_id
-- Chạy script này để fix lỗi login

USE cnpm;

-- Xem cấu trúc bảng log hiện tại
DESCRIBE log;

-- Sửa log_id thành AUTO_INCREMENT
ALTER TABLE log MODIFY COLUMN log_id INT NOT NULL AUTO_INCREMENT;

-- Kiểm tra lại cấu trúc
DESCRIBE log;

-- Test: Thử insert một record
-- INSERT INTO log (content, date_log, user_id) VALUES ('Test log', NOW(), 1);
-- SELECT * FROM log;
