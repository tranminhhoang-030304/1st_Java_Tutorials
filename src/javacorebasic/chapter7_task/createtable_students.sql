CREATE TABLE students (
     id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(100) UNIQUE,
     gender VARCHAR(10),
     hometown VARCHAR(100),
     age INT
);