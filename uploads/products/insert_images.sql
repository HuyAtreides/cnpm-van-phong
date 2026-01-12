-- Script để thêm ảnh sản phẩm vào database
-- Hướng dẫn: 
-- 1. Đặt ảnh vào thư mục: uploads/products/
-- 2. Chạy script này để thêm đường dẫn ảnh vào database

USE cnpm;

-- Xóa ảnh cũ (nếu có)
DELETE FROM product_image;

-- Thêm ảnh cho sản phẩm 1: Electric Standing Desk Pro
INSERT INTO product_image (productImageId, productImage, productId) VALUES 
(1, 'uploads/products/desk1.jpg', 1),
(2, 'uploads/products/desk1-2.jpg', 1),
(3, 'uploads/products/desk1-3.jpg', 1);

-- Thêm ảnh cho sản phẩm 2: L-Shaped Executive Desk
INSERT INTO product_image (productImageId, productImage, productId) VALUES 
(4, 'uploads/products/desk2.jpg', 2),
(5, 'uploads/products/desk2-2.jpg', 2);

-- Thêm ảnh cho sản phẩm 3: Compact Writing Desk
INSERT INTO product_image (productImageId, productImage, productId) VALUES 
(6, 'uploads/products/desk3.jpg', 3);

-- Thêm ảnh cho sản phẩm 4: Industrial Style Desk
INSERT INTO product_image (productImageId, productImage, productId) VALUES 
(7, 'uploads/products/desk4.jpg', 4);

-- Thêm ảnh cho sản phẩm 5: Ergonomic Mesh Chair Pro
INSERT INTO product_image (productImageId, productImage, productId) VALUES 
(8, 'uploads/products/chair1.jpg', 5),
(9, 'uploads/products/chair1-2.jpg', 5),
(10, 'uploads/products/chair1-3.jpg', 5);

-- Thêm ảnh cho sản phẩm 6: Executive Leather Chair
INSERT INTO product_image (productImageId, productImage, productId) VALUES 
(11, 'uploads/products/chair2.jpg', 6),
(12, 'uploads/products/chair2-2.jpg', 6);

-- Thêm ảnh cho sản phẩm 7: Basic Task Chair
INSERT INTO product_image (productImageId, productImage, productId) VALUES 
(13, 'uploads/products/chair3.jpg', 7);

-- Thêm ảnh cho sản phẩm 8: Gaming Office Chair
INSERT INTO product_image (productImageId, productImage, productId) VALUES 
(14, 'uploads/products/chair4.jpg', 8);

-- Thêm ảnh cho sản phẩm 9: Mobile Pedestal Cabinet
INSERT INTO product_image (productImageId, productImage, productId) VALUES 
(15, 'uploads/products/cabinet1.jpg', 9);

-- Thêm ảnh cho sản phẩm 10: Modern Bookshelf
INSERT INTO product_image (productImageId, productImage, productId) VALUES 
(16, 'uploads/products/bookshelf1.jpg', 10);

-- Kiểm tra kết quả
SELECT p.productId, p.name, COUNT(pi.productImageId) as image_count
FROM product p
LEFT JOIN product_image pi ON p.productId = pi.productId
GROUP BY p.productId, p.name
ORDER BY p.productId;
