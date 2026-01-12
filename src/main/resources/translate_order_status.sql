-- Translate order status from English to Vietnamese
USE cnpm;

-- Update order statuses
UPDATE orders SET status = 'Đang xử lý' WHERE status = 'Processing';
UPDATE orders SET status = 'Hoàn thành' WHERE status = 'Completed';
UPDATE orders SET status = 'Đã hủy' WHERE status = 'Cancelled';

-- Verify the update
SELECT order_id, customer_id, status, order_date 
FROM orders 
ORDER BY order_id DESC 
LIMIT 10;
