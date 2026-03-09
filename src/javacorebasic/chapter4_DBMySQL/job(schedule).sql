-- Bước 1: Bật cỗ máy thời gian (Scheduler) của MySQL lên
SET GLOBAL event_scheduler = ON;

-- Bước 2: Tạo một Job (Event) chạy tự động
CREATE EVENT don_dep_du_lieu_rac
ON SCHEDULE EVERY 1 DAY -- Hẹn giờ: Chạy mỗi ngày 1 lần
STARTS CURRENT_TIMESTAMP -- Bắt đầu tính từ ngay lúc này
DO
-- Xóa các bản ghi không sinh ra doanh thu (ví dụ)
DELETE FROM doanh_thu_digital_product WHERE doanh_thu = 0;