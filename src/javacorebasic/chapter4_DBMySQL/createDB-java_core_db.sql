-- 1. TẠO DATABASE
CREATE DATABASE IF NOT EXISTS java_core_db;
USE java_core_db;

-- 2. TẠO TABLE & SEQUENCE
-- ( Oracle dùng SEQUENCE rời, MySQL tích hợp sẵn chuỗi tăng tự động qua từ khóa AUTO_INCREMENT)
CREATE TABLE pro_baller (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            ten VARCHAR(100) NOT NULL,
                            gioi_tinh VARCHAR(10),
                            quoc_gia VARCHAR(100),
                            tuoi INT
);

-- 3. CÁC CÂU LỆNH CƠ BẢN (CRUD)
INSERT INTO pro_baller (ten, gioi_tinh, quoc_gia, tuoi) VALUES ('Lebron James', 'Nam', 'USA', 42);
INSERT INTO pro_baller (ten, gioi_tinh, quoc_gia, tuoi) VALUES ('Shai-Gigeous Alexander', 'Nam', 'Canada', 29);
INSERT INTO pro_baller (ten, gioi_tinh, quoc_gia, tuoi) VALUES ('Luka Doncic', 'Nam', 'Slovenia', 27);
INSERT INTO pro_baller (ten, gioi_tinh, quoc_gia, tuoi) VALUES ('Nikola Jokic', 'Nam', 'Serbia', 32);
INSERT INTO pro_baller (ten, gioi_tinh, quoc_gia, tuoi) VALUES ('Tony Parker', 'Nam', 'France', 48);
INSERT INTO pro_baller (ten, gioi_tinh, quoc_gia, tuoi) VALUES ('Alpere Sengun', 'Nữ', 'Turkey', 27);
INSERT INTO pro_baller (ten, gioi_tinh, quoc_gia, tuoi) VALUES ('Devin Booker', 'Nữ', 'USA', 31);
SELECT * FROM pro_baller;

-- 4. STORE PROCEDURE (Thủ tục lưu trữ)
-- Giúp đóng gói một cụm logic SQL phức tạp để gọi lại nhiều lần
DELIMITER //
CREATE PROCEDURE GetBallerByCountry(IN p_quoc_gia VARCHAR(100))
BEGIN
SELECT * FROM pro_baller WHERE quoc_gia = p_quoc_gia;
END //
DELIMITER ;
-- Cách gọi procedure này: CALL GetBallerByCountry('Viet Nam');

-- 5. FUNCTION (Hàm)
-- Khác Procedure, Function BẮT BUỘC phải trả về một giá trị (RETURN)
DELIMITER //
CREATE FUNCTION CountBaller() RETURNS INT DETERMINISTIC
BEGIN
    DECLARE total INT;
SELECT COUNT(*) INTO total FROM pro_baller;
RETURN total;
END //
DELIMITER ;
-- Cách gọi function: SELECT CountBaller();

-- 6. TRIGGER (Trình kích hoạt)
-- Tự động chạy ngầm khi có sự kiện INSERT/UPDATE/DELETE
DELIMITER //
CREATE TRIGGER check_tuoi_truoc_insert
    BEFORE INSERT ON pro_baller
    FOR EACH ROW
BEGIN
    -- Nếu tuổi nhập vào bị âm, trigger tự động sửa thành 0 trước khi lưu
    IF NEW.tuoi < 0 THEN
        SET NEW.tuoi = 0;
END IF;
END //
DELIMITER ;