package javacoreadvance.chapter3_networkprogramme.UDP;
import java.net.*;
public class UDPSender {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket = new DatagramSocket();
        String message = "Gói tin UDP!";
        byte[] data = message.getBytes();
        InetAddress address = InetAddress.getByName("localhost"); //Đóng gói tin nhắn, ghi rõ địa chỉ đúng (localhost, port) để gửi đi
        DatagramPacket packet = new DatagramPacket(data, data.length, address, 9999);
        socket.send(packet);
        System.out.println("Đã gửi 1 gói tin!");
        socket.close();
    }
}
