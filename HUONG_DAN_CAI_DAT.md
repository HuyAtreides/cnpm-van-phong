# Hướng Dẫn Cài Đặt và Chạy Ứng Dụng Ergoffice

## 📋 Mục Lục

1. [Yêu Cầu Hệ Thống](#yêu-cầu-hệ-thống)
2. [Công Nghệ Sử Dụng](#công-nghệ-sử-dụng)
3. [Cài Đặt Môi Trường](#cài-đặt-môi-trường)
4. [Cấu Hình Database](#cấu-hình-database)
5. [Chạy Ứng Dụng](#chạy-ứng-dụng)
6. [Truy Cập Ứng Dụng](#truy-cập-ứng-dụng)
7. [Xử Lý Lỗi Thường Gặp](#xử-lý-lỗi-thường-gặp)

---

## 🖥️ Yêu Cầu Hệ Thống

### Phần Cứng Tối Thiểu

- **CPU**: Intel Core i3 hoặc tương đương
- **RAM**: 4GB (khuyến nghị 8GB)
- **Ổ cứng**: 2GB dung lượng trống

### Hệ Điều Hành

- Windows 10/11
- macOS 10.14+
- Linux (Ubuntu 18.04+, CentOS 7+)

---

## 🛠️ Công Nghệ Sử Dụng

### Backend

- **Ngôn ngữ**: Java 17 (JDK 17.0.12)
- **Framework**: Spring Boot 3.3.1
  - Spring MVC (Web)
  - Spring Data JPA (Database)
  - Spring Security (Authentication)
  - Thymeleaf (Template Engine)
- **Build Tool**: Maven 3.9+

### Frontend

- **HTML5** + **CSS3**
- **JavaScript** (Vanilla JS)
- **Bootstrap 5.3.0** (UI Framework)
- **Chart.js 4.4.0** (Biểu đồ thống kê)
- **Font Awesome 6.4.0** (Icons)

### Database

- **MySQL 8.0+**
- **Hibernate** (ORM)

---

## 📦 Cài Đặt Môi Trường

### Bước 1: Cài Đặt Java Development Kit (JDK) 17

#### Windows:

1. Tải JDK 17 từ [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. Chạy file cài đặt `.exe`
3. Chọn đường dẫn cài đặt (mặc định: `C:\Program Files\Java\jdk-17`)
4. Thiết lập biến môi trường:
   ```powershell
   # Mở PowerShell với quyền Administrator
   [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")
   [System.Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Program Files\Java\jdk-17\bin", "Machine")
   ```
5. Kiểm tra cài đặt:
   ```powershell
   java -version
   # Kết quả: java version "17.0.12"
   ```

#### macOS:

```bash
# Sử dụng Homebrew
brew install openjdk@17

# Thiết lập JAVA_HOME
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc

# Kiểm tra
java -version
```

#### Linux (Ubuntu/Debian):

```bash
sudo apt update
sudo apt install openjdk-17-jdk

# Thiết lập JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
source ~/.bashrc

# Kiểm tra
java -version
```

### Bước 2: Cài Đặt Maven

#### Windows:

1. Tải Maven từ [Apache Maven](https://maven.apache.org/download.cgi)
2. Giải nén vào `C:\Program Files\Apache\maven`
3. Thêm vào PATH:
   ```powershell
   [System.Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Program Files\Apache\maven\bin", "Machine")
   ```
4. Kiểm tra:
   ```powershell
   mvn -version
   ```

#### macOS/Linux:

```bash
# macOS
brew install maven

# Linux
sudo apt install maven

# Kiểm tra
mvn -version
```

### Bước 3: Cài Đặt MySQL

#### Windows:

1. Tải MySQL Installer từ [MySQL Downloads](https://dev.mysql.com/downloads/installer/)
2. Chạy MySQL Installer
3. Chọn "Developer Default" setup type
4. Cấu hình MySQL Server:
   - **Port**: 3306 (mặc định)
   - **Root Password**: Đặt mật khẩu mạnh (ví dụ: `root123`)
   - **Authentication**: Use Strong Password Encryption
5. Hoàn tất cài đặt

#### macOS:

```bash
brew install mysql
brew services start mysql

# Thiết lập root password
mysql_secure_installation
```

#### Linux:

```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo mysql_secure_installation
```

### Bước 4: Cài Đặt MySQL Workbench (Tùy chọn)

- Tải từ [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)
- Công cụ GUI để quản lý database dễ dàng hơn

---

## 🗄️ Cấu Hình Database

### Bước 1: Tạo Database

#### Sử dụng MySQL Command Line:

```bash
# Đăng nhập MySQL
mysql -u root -p
# Nhập password đã đặt ở bước cài đặt

# Tạo database
CREATE DATABASE ergoffice CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Tạo user cho ứng dụng (khuyến nghị)
CREATE USER 'ergoffice_user'@'localhost' IDENTIFIED BY 'ergoffice_pass';
GRANT ALL PRIVILEGES ON ergoffice.* TO 'ergoffice_user'@'localhost';
FLUSH PRIVILEGES;

# Kiểm tra
SHOW DATABASES;
USE ergoffice;

# Thoát
EXIT;
```

#### Sử dụng MySQL Workbench:

1. Mở MySQL Workbench
2. Kết nối đến MySQL Server (localhost:3306)
3. Click chuột phải vào "Schemas" → "Create Schema"
4. Nhập tên: `ergoffice`
5. Charset: `utf8mb4`, Collation: `utf8mb4_unicode_ci`
6. Click "Apply"

### Bước 2: Cấu Hình File application.properties

Mở file `src/main/resources/application.properties` và cập nhật:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ergoffice?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080

# Thymeleaf Configuration
spring.thymeleaf.cache=false

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

**Lưu ý**:

- Thay `root123` bằng password MySQL của bạn
- Nếu dùng user `ergoffice_user`, thay username và password tương ứng

### Bước 3: Tự Động Tạo Bảng

Ứng dụng sử dụng **Hibernate Auto DDL** với `spring.jpa.hibernate.ddl-auto=update`:

- Khi chạy lần đầu, Hibernate sẽ **tự động tạo tất cả các bảng** trong database
- Các bảng được tạo:
  - `user` (Người dùng)
  - `customer` (Khách hàng)
  - `employee` (Nhân viên)
  - `category` (Danh mục)
  - `product` (Sản phẩm)
  - `product_type` (Phân loại sản phẩm)
  - `product_image` (Hình ảnh sản phẩm)
  - `cart` (Giỏ hàng)
  - `cart_item` (Sản phẩm trong giỏ)
  - `order` (Đơn hàng)
  - `order_item` (Chi tiết đơn hàng)
  - `review` (Đánh giá)
  - `voucher` (Voucher)
  - `voucher_by_price` (Voucher theo giá)
  - `refund_request` (Yêu cầu hoàn tiền)
  - `address` (Địa chỉ giao hàng)

---

## 🚀 Chạy Ứng Dụng

### Phương Pháp 1: Sử Dụng Maven (Khuyến nghị)

#### Bước 1: Clone hoặc Giải nén Source Code

```bash
cd C:\Users\Acer\IdeaProjects\van-phong
```

#### Bước 2: Build Project

```bash
# Clean và build project
mvn clean install -DskipTests

# Kết quả thành công:
# [INFO] BUILD SUCCESS
# [INFO] Total time: 5-10 seconds
```

#### Bước 3: Chạy Ứng Dụng

```bash
mvn spring-boot:run -DskipTests
```

**Hoặc nếu gặp lỗi Java version:**

```powershell
# Windows PowerShell
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
mvn spring-boot:run -DskipTests
```

```bash
# macOS/Linux
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
mvn spring-boot:run -DskipTests
```

#### Bước 4: Kiểm Tra Khởi Động Thành Công

Khi thấy log:

```
2026-01-09 12:30:00.000  INFO 12345 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http)
2026-01-09 12:30:00.000  INFO 12345 --- [  restartedMain] o.e.vanphong.VanPhongApplication         : Started VanPhongApplication in 15.234 seconds
```

✅ **Ứng dụng đã chạy thành công!**

### Phương Pháp 2: Sử Dụng IDE (IntelliJ IDEA / Eclipse)

#### IntelliJ IDEA:

1. Mở IntelliJ IDEA
2. File → Open → Chọn thư mục project
3. Đợi Maven import dependencies (có thể mất 5-10 phút lần đầu)
4. Tìm file `VanPhongApplication.java` trong `src/main/java/org/example/vanphong/`
5. Click chuột phải → Run 'VanPhongApplication'
6. Hoặc nhấn nút ▶️ màu xanh ở góc trên phải

#### Eclipse:

1. Mở Eclipse
2. File → Import → Maven → Existing Maven Projects
3. Chọn thư mục project → Finish
4. Đợi Maven build workspace
5. Chuột phải vào project → Run As → Spring Boot App

### Phương Pháp 3: Chạy File JAR (Production)

```bash
# Build JAR file
mvn clean package -DskipTests

# Chạy JAR
java -jar target/van-phong-1.0-SNAPSHOT.jar

# Hoặc chỉ định port khác
java -jar target/van-phong-1.0-SNAPSHOT.jar --server.port=9090
```

---

## 🌐 Truy Cập Ứng Dụng

### URL Chính

- **Trang chủ**: http://localhost:8080
- **Đăng nhập**: http://localhost:8080/login
- **Đăng ký**: http://localhost:8080/register
- **Admin Panel**: http://localhost:8080/admin

### Tài Khoản Mặc Định

Sau khi chạy lần đầu, hệ thống tự động tạo tài khoản admin:

**Admin:**

- Username: `admin`
- Password: `admin123`
- Role: ADMIN

**Test User:**

- Username: `user`
- Password: `user123`
- Role: CUSTOMER

### Các Trang Chính

#### User Side:

- 🏠 Trang chủ: `/`
- 📦 Sản phẩm: `/products`
- 🛒 Giỏ hàng: `/cart`
- 💳 Thanh toán: `/checkout`
- 📋 Đơn hàng: `/orders`
- 👤 Tài khoản: `/profile`
- ⭐ Đánh giá: `/reviews`

#### Admin Side:

- 📊 Dashboard: `/admin`
- 📈 Thống kê: `/admin/stats`
- 📦 Quản lý sản phẩm: `/admin/products`
- 👥 Quản lý khách hàng: `/admin/customers`
- 🎫 Quản lý voucher: `/admin/vouchers`
- 📋 Quản lý đơn hàng: `/admin/orders`
- 💰 Yêu cầu hoàn tiền: `/admin/refunds`

---

## ⚠️ Xử Lý Lỗi Thường Gặp

### 1. Lỗi: "Port 8080 already in use"

**Nguyên nhân**: Port 8080 đang được sử dụng bởi ứng dụng khác

**Giải pháp**:

#### Windows:

```powershell
# Tìm process đang dùng port 8080
netstat -ano | findstr :8080

# Kill process (thay PID bằng số process ID)
taskkill /F /PID <PID>
```

#### macOS/Linux:

```bash
# Tìm process
lsof -i :8080

# Kill process
kill -9 <PID>
```

**Hoặc đổi port**:

```bash
mvn spring-boot:run -Dserver.port=9090
```

### 2. Lỗi: "Unsupported class file major version 68"

**Nguyên nhân**: Maven đang dùng Java 24 thay vì Java 17

**Giải pháp**:

```powershell
# Windows
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
mvn clean install -DskipTests
mvn spring-boot:run -DskipTests
```

```bash
# macOS/Linux
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
mvn clean install -DskipTests
mvn spring-boot:run -DskipTests
```

### 3. Lỗi: "Access denied for user 'root'@'localhost'"

**Nguyên nhân**: Sai username/password MySQL

**Giải pháp**:

1. Kiểm tra file `application.properties`
2. Đảm bảo username và password đúng
3. Reset MySQL password nếu cần:

```bash
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;
```

### 4. Lỗi: "Communications link failure"

**Nguyên nhân**: MySQL Server chưa chạy

**Giải pháp**:

#### Windows:

```powershell
# Mở Services (Win + R → services.msc)
# Tìm "MySQL80" → Start
```

#### macOS:

```bash
brew services start mysql
```

#### Linux:

```bash
sudo systemctl start mysql
sudo systemctl status mysql
```

### 5. Lỗi: "Table 'ergoffice.user' doesn't exist"

**Nguyên nhân**: Database chưa được tạo hoặc Hibernate chưa tạo bảng

**Giải pháp**:

1. Kiểm tra database `ergoffice` đã tồn tại:

```sql
SHOW DATABASES;
```

2. Kiểm tra `application.properties`:

```properties
spring.jpa.hibernate.ddl-auto=update
```

3. Xóa database và để Hibernate tạo lại:

```sql
DROP DATABASE ergoffice;
CREATE DATABASE ergoffice CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

4. Restart ứng dụng

### 6. Lỗi: "Failed to load ApplicationContext"

**Nguyên nhân**: Lỗi cấu hình Spring Boot

**Giải pháp**:

```bash
# Clean toàn bộ và build lại
mvn clean
rm -rf target/
mvn install -DskipTests
mvn spring-boot:run
```

---

## 📝 Ghi Chú Quan Trọng

### Database Auto-Seeding

- Khi chạy lần đầu, ứng dụng tự động:
  - Tạo tài khoản admin/user
  - Tạo categories mẫu
  - Import sản phẩm từ thư mục `images/` (nếu có)
  - Tạo vouchers mẫu

### Thư Mục Quan Trọng

```
van-phong/
├── src/
│   ├── main/
│   │   ├── java/              # Source code Java
│   │   └── resources/
│   │       ├── application.properties  # Cấu hình chính
│   │       ├── templates/     # Thymeleaf templates
│   │       └── static/        # CSS, JS, Images
├── target/                    # Build output
├── pom.xml                    # Maven dependencies
└── HUONG_DAN_CAI_DAT.md      # File này
```

### Logs

- Logs được hiển thị trực tiếp trong console
- Để lưu logs vào file, thêm vào `application.properties`:

```properties
logging.file.name=logs/application.log
logging.level.org.example.vanphong=DEBUG
```

### Backup Database

```bash
# Backup
mysqldump -u root -p ergoffice > backup_ergoffice.sql

# Restore
mysql -u root -p ergoffice < backup_ergoffice.sql
```

---

## 🎯 Kiểm Tra Hoàn Tất

Checklist sau khi cài đặt:

- [ ] Java 17 đã cài đặt (`java -version`)
- [ ] Maven đã cài đặt (`mvn -version`)
- [ ] MySQL đã chạy (`mysql -u root -p`)
- [ ] Database `ergoffice` đã tạo
- [ ] File `application.properties` đã cấu hình đúng
- [ ] Build thành công (`mvn clean install`)
- [ ] Ứng dụng chạy được (`mvn spring-boot:run`)
- [ ] Truy cập http://localhost:8080 thành công
- [ ] Đăng nhập admin thành công

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề không có trong tài liệu này:

1. Kiểm tra logs trong console
2. Kiểm tra MySQL logs
3. Google error message cụ thể
4. Liên hệ team phát triển

**Chúc bạn cài đặt thành công! 🎉**
