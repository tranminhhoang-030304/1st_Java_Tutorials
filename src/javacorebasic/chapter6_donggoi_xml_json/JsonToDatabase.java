package javacorebasic.chapter6_donggoi_xml_json;

import com.google.gson.Gson;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

// Khuôn dữ liệu
class TrackingEventDB {
    String eventName;
    String deviceId;
    double revenue;

    public TrackingEventDB(String eventName, String deviceId, double revenue) {
        this.eventName = eventName;
        this.deviceId = deviceId;
        this.revenue = revenue;
    }
}

public class JsonToDatabase {
    public static void main(String[] args) {
        // --- BƯỚC 1: NHẬN VÀ BÓC TÁCH JSON (Mô phỏng dữ liệu từ AppMetrica gửi về) ---
        String incomingJson = "{\"eventName\":\"level_up\",\"deviceId\":\"Device-JSON007\",\"revenue\":5.99}";

        Gson gson = new Gson();
        TrackingEvent parsedEvent = gson.fromJson(incomingJson, TrackingEvent.class);
        System.out.println("✅ Đã bóc tách JSON thành Object: Sự kiện " + parsedEvent.eventName);

        // --- BƯỚC 2: ĐỌC CONFIG ĐỂ TÌM ĐƯỜNG VÀO DATABASE ---
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("database.properties")) {
            props.load(fis);
        } catch (Exception e) {
            System.err.println("❌ Lỗi đọc config: Không thấy file database.properties");
            return;
        }

        String dbUrl = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        // --- BƯỚC 3: KẾT NỐI MYSQL VÀ LƯU DỮ LIỆU JSON XUỐNG BẢNG ---
        System.out.println("Đang mở kết nối xuống DB...");
        try (Connection conn = DriverManager.getConnection(dbUrl, user, pass)) {

            // Dùng PreparedStatement nhét dữ liệu từ Object vừa parse vào câu lệnh SQL
            String sql = "INSERT INTO tracking_event (event_name, device_id, revenue) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, parsedEvent.eventName);
                pstmt.setString(2, parsedEvent.deviceId);
                pstmt.setDouble(3, parsedEvent.revenue);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("🎉 THÀNH CÔNG: Đã lưu sự kiện [" + parsedEvent.eventName + "] từ JSON vào database MySQL!");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lưu DB: " + e.getMessage());
        }
    }
}
