/**
 +) Cần 2 luồng song song
 -) Luồng chính lắng nghe bàn phím xem có 'stop' không
 -) Luồng phụ sinh số ngẫu nhiên và ghi ra file ouput.txt
 -) Dùng cờ volatile boolean để luồng Main gọi dừng luồng phụ
 */

package javacoreadvance.chapter6;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class a_realtime_writetoutputfile {
    private static volatile boolean isRunning = true; // Dùng volatile để đảm bảo khi Main thay đổi, luồng ghi file nhận được tín hiệu ngay lập tức
    public static void main(String[] args){
        System.out.println("Chương trình đang chạy. 'Stop' để dừng ... ");
        Thread writerThread = new Thread(() ->{ // Luồng phụ: Ghi file liên tục
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                Random random = new Random();
                while(isRunning){
                    int num = random.nextInt(1000);
                    writer.write(num + "\n");
                    writer.flush(); // Đẩy ngay xuống file
                    Thread.sleep(500); // Ngủ 0.5s cho đỡ đầy ổ cứng quá nhanh
                }
                System.out.println("Luồng ghi file đã dừng hoàn toàn!");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        writerThread.start();
        Scanner scanner = new Scanner(System.in); // Luồng Main: Lắng nghe bàn phím
        while (isRunning){
            String input = scanner.nextLine();
            if("stop".equalsIgnoreCase(input.trim())) {
                isRunning = false; // Ra lệnh cho luồng phụ dừng lại
            }
        }
        scanner.close();
    }
}
