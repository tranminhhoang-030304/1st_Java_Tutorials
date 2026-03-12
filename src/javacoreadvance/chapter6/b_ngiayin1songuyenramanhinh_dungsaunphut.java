//Dùng thời gian hệ thống (System.currentTimeMillis()) để tính tóan điểm dừng thay vì dùng phím tắt

package javacoreadvance.chapter6;

import java.util.Random;
import java.util.Scanner;

public class b_ngiayin1songuyenramanhinh_dungsaunphut {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhập khoảng thời gian in số (n giây):");
        int intervalSeconds = scanner.nextInt();
        System.out.println("Nhập thời gian chạy tối đa (n phút): ");
        int durationMinutes = scanner.nextInt();
        scanner.close();

        long startTime = System.currentTimeMillis();
        long endTime = startTime + ((long) + durationMinutes * 60 * 1000); // Đổi phút ra mili-giây
        int intervalMillis = intervalSeconds * 1000;

        System.out.println("Bắt đầu chạy. Sẽ dừng tự động sau ... " + durationMinutes + " phút...");
        Random random = new Random();

        while (System.currentTimeMillis() < endTime) {
            System.out.println("Số ngẫu nhiên: " + random.nextInt(100));
            try {
                Thread.sleep(intervalMillis); // Ngủ n giây rồi in tiếp
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Đã hết thời gian! Chương trình kết thúc!");
    }
}
