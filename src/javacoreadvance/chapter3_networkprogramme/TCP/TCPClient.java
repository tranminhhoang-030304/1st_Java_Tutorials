package javacoreadvance.chapter3_networkprogramme.TCP;
import java.io.*;
import java.net.*;
public class TCPClient {
    public static void main (String[] args) throws Exception{
        Socket socket = new Socket("localhost", 8888); //gọi đến đúng địa chỉ localhost, cổng 8888
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //gửi tin nhắn
        out.println("Đây là máy khách kết nối bằng TCP!");
        socket.close();
    }
}
