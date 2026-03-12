package javacoreadvance.chapter6.d_network_task;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.UUID;

public class MachineA_Client {
    private static final Logger logger = LoggerFactory.getLogger(MachineA_Client.class);
    public static void main(String[] args) {
        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream("config6d.properties")) {
            config.load(fis);
        } catch (IOException e) {
            logger.error("Không tìm thấy file config6d.properties!", e);
            return;
        }
        String ip = config.getProperty("server_ip");
        int port = Integer.parseInt(config.getProperty("server_port"));
        int connTimeout = Integer.parseInt(config.getProperty("connection_timeout"));
        int rwTimeout = Integer.parseInt(config.getProperty("send_receive_timeout"));
        try (Socket socket = new Socket()) {
            logger.info("Đang kết nối tới máy B ({} : {})...",ip,port);
            socket.connect(new InetSocketAddress(ip,port), connTimeout);
            socket.setSoTimeout(rwTimeout);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (true) { // Tạo chuỗi ngẫu nhiên
                String randomString = "Data-" + UUID.randomUUID().toString().substring(0, 8);
                out.println(randomString);
                logger.info("Đã gửi: {}", randomString);
                Thread.sleep(1000); // Nghỉ 1s rồi gửi tiếp
            }
        } catch (Exception e) {
            logger.error("Lỗi truyền data trên máy A! (Mạng hoặc cáp quang!)", e);
        }
    }
}
