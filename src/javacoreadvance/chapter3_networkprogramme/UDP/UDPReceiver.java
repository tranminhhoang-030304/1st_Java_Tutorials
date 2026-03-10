package javacoreadvance.chapter3_networkprogramme.UDP;
import java.net.*;
public class UDPReceiver {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket = new DatagramSocket(9999); //mở cổng 9999 để hứng data
        byte[] buffer = new byte[1024]; //tạo không gian tiếp nhận data
        System.out.println("Đang chờ gói tin UDP...");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet); //block luồng cho đến khi có gói tin gửi tới
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Đã tìm thấy gói tin: " + message);
        socket.close();
    }
}
