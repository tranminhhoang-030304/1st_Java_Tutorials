-- Tạo bảng có phân vùng (Partition) theo Năm
CREATE TABLE doanh_thu_digital_product (
    id INT NOT NULL,
    ten_san_pham VARCHAR(100),
    ngay_mua DATE NOT NULL,
    doanh_thu DECIMAL(10,2)
)
-- Khai báo chia vùng dựa vào cột 'ngay_mua'
    PARTITION BY RANGE (YEAR(ngay_mua)) (
    PARTITION p_2025 VALUES LESS THAN (2026), -- Vùng chứa data năm 2025
    PARTITION p_2026 VALUES LESS THAN (2027), -- Vùng chứa data năm 2026
    PARTITION p_tuong_lai VALUES LESS THAN MAXVALUE -- Vùng chứa các năm sau này
);