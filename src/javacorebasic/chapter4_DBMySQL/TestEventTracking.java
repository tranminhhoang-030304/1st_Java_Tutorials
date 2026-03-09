package javacorebasic.chapter4_DBMySQL;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TestEventTracking {

    public static void main(String[] args) {
        // --- BƯỚC 1: ĐỌC THÔNG TIN TỪ FILE CONFIG ---
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("database.properties")) {
            props.load(fis);
        } catch (Exception e) {
            System.err.println("❌ Lỗi: Không thể khởi động do thiếu file database.properties!");
            return;
        }

        String dbUrl = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        System.out.println("Đang khởi động hệ thống Event Tracking...");

        // --- BƯỚC 2: KẾT NỐI VÀ THỰC THI NGHIỆP VỤ ---
        try (Connection conn = DriverManager.getConnection(dbUrl, user, pass)) {

            // 2.a: INSERT DỮ LIỆU
            String insertSql = "INSERT INTO tracking_event (event_name, device_id, revenue) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

                pstmt.setString(1, "app_install");
                pstmt.setString(2, "Device-A100");
                pstmt.setDouble(3, 0.0);
                pstmt.executeUpdate();

                pstmt.setString(1, "in_app_purchase");
                pstmt.setString(2, "Device-B200");
                pstmt.setDouble(3, 99.99);
                pstmt.executeUpdate();

                System.out.println("✅ Đã ghi nhận 2 sự kiện tracking thành công vào Database!\n");
            }

            // 2.b: SELECT DỮ LIỆU
            String selectSql = "SELECT * FROM tracking_event";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSql)) {

                System.out.println("--- BÁO CÁO SỰ KIỆN HỆ THỐNG ---");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String eventName = rs.getString("event_name");
                    String device = rs.getString("device_id");
                    double revenue = rs.getDouble("revenue");
                    String time = rs.getString("created_at");

                    System.out.println(String.format("[%s] Event: %s | Thiết bị: %s | Doanh thu: $%.2f",
                            time, eventName, device, revenue));
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Có lỗi xảy ra trong quá trình tracking: " + e.getMessage());
        }
    }
}