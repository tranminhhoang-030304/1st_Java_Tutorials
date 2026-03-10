package javacoreadvance.chapter3_networkprogramme.TCP;
import java.net.*;
import java.io.*;
public class TCPServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8888); //mở port 8888
        System.out.println("Máy chủ mở kết nối tại cổng 8888...");
        Socket socket = serverSocket.accept(); //block luồng cho đến khi có máy khách kết nối
        System.out.println("Có kết nối!");
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //đọc gói data máy khách gửi đến
        String message = in.readLine();
        System.out.println("Máy khách phản hồi: " + message);
        serverSocket.close();
    }
}
