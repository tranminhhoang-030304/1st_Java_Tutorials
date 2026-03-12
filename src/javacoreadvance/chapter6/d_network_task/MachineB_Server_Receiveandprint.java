package javacoreadvance.chapter6.d_network_task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;

public class MachineB_Server_Receiveandprint {
    private static final Logger logger = LoggerFactory.getLogger(MachineB_Server_Receiveandprint.class);
    public static void main(String[] args) {
        int port = 9999;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Máy B đang chờ kết nối tại port {}...", port);
            Socket clientSocket = serverSocket.accept();
            clientSocket.setSoTimeout(3000);
            logger.info("Máy A đã kết nối: {}", clientSocket.getInetAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            while ((message = in.readLine()) != null){
                System.out.println("[Máy B nhận được tín hiệu]: " + message);
            }
        } catch (Exception e) {
            logger.error("Lỗi kêt nối trên máy B! (Do mạng hoặc Timeout!)", e);
        }
    }
}
