CREATE TABLE tracking_event (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                event_name VARCHAR(50) NOT NULL,
                                device_id VARCHAR(100),
                                revenue DECIMAL(10,2),
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);