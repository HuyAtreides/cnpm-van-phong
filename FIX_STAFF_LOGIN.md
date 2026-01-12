# Hướng dẫn sửa lỗi đăng nhập Staff

## Vấn đề
Không thể đăng nhập với tài khoản staff vì:
1. Role trong database không khớp với code (EMPLOYEE vs STAFF)
2. Tài khoản staff chưa được tạo đúng cách

## Cách sửa

### Bước 1: Chạy script SQL
Mở MySQL Workbench hoặc command line MySQL và chạy file:
```
src/main/resources/fix_staff_login.sql
```

Hoặc copy và chạy trực tiếp các lệnh SQL trong file đó.

### Bước 2: Khởi động lại ứng dụng
```bash
run_fixed.bat
```

### Bước 3: Đăng nhập
- **Email:** staff@ergoffice.com
- **Password:** 123456

HOẶC

- **Email:** nhanvien@ergoffice.com  
- **Password:** 123123

## Lưu ý
- Script sẽ xóa và tạo lại role ID 2, 3, 4
- Tài khoản staff sẽ có userId 9001 và 9002 để tránh xung đột
- Mật khẩu sử dụng {noop} prefix để lưu dạng plain text (chỉ dùng cho development)
