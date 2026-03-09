package javacorebasic.chapter4_DBMySQL;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TestMySQL {

    public static void main(String[] args) {
        // --- BƯỚC 1: ĐỌC THÔNG TIN TỪ FILE CONFIG ---
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("database.properties")) {
            props.load(fis);
        } catch (Exception e) {
            System.err.println("❌ Lỗi: Không thể tìm thấy hoặc không đọc được file database.properties!");
            return; // Dừng chương trình ngay lập tức nếu không có file config
        }

        // Lấy thông tin đã đọc được gán vào các biến chuỗi
        String dbUrl = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");
        String port = props.getProperty("db.port");
        String host = props.getProperty("db.host");

        System.out.println("Đang kết nối đến MySQL bằng cấu hình từ file properties...");

        // --- BƯỚC 2: KẾT NỐI VÀ TRUY VẤN DATABASE ---
        // Truyền trực tiếp các biến cấu hình vào DriverManager
        try (Connection conn = DriverManager.getConnection(dbUrl, user, pass);
             Statement stmt = conn.createStatement()) {

            System.out.println("✅ Kết nối database THÀNH CÔNG RỰC RỠ!");

            String sql = "SELECT * FROM pro_baller";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("--- DANH SÁCH PRO BALLER ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                String ten = rs.getString("ten");
                String quocGia = rs.getString("quoc_gia");
                int tuoi = rs.getInt("tuoi");

                System.out.println("ID: " + id + " | Tên: " + ten + " | Quốc gia: " + quocGia + " | Tuổi: " + tuoi);
            }

        } catch (Exception e) {
            System.err.println("❌ KẾT NỐI THẤT BẠI: " + e.getMessage());
        }
    }
}