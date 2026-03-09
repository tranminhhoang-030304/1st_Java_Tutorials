package javacorebasic.chapter5_fileconfig;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class ConfigReader {
    public static void main(String[] args) {
        // Sử dụng class Properties có sẵn của lõi Java để đọc file config
        Properties properties = new Properties();

        // Dùng try-with-resources để đọc file
        try (FileInputStream fis = new FileInputStream("database.properties")) {

            // Nạp dữ liệu từ file vào đối tượng properties
            properties.load(fis);

            // Lấy thông tin ra bằng cách truyền vào cái 'key' (tên biến)
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String pass = properties.getProperty("db.password");
            String port = properties.getProperty("db.port");
            String host = properties.getProperty("db.host");

            System.out.println("--- THÔNG TIN LẤY TỪ FILE CONFIG ---");
            System.out.println("URL Database: " + url);
            System.out.println("Username: " + user);
            System.out.println("Password: " + pass);
            System.out.println("Port: " + port);
            System.out.println("Host: " + host);

        } catch (IOException e) {
            System.err.println("❌ Không tìm thấy hoặc không đọc được file config: " + e.getMessage());
        }
    }
}
