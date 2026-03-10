package javacoreadvance.chapter4_multithread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class b_QueueDemo {
    public static void main(String[] args) {
        // Tạo cái bàn đặt thức ăn chỉ chứa tối đa 3 món
        BlockingQueue<String> table = new ArrayBlockingQueue<>(3);

        // Luồng 1: Đầu bếp (Nấu ăn và đặt lên bàn)
        Thread chef = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    String food = "Món số " + i;
                    System.out.println("Đã nấu xong: " + food + " -> Đang lên mâm...");

                    // Dùng lệnh put() của BlockingQueue (Nếu bàn đầy 3 món, tự động đứng chờ)
                    table.put(food);
                    System.out.println("Đã lên mâm. Trên mâm có: " + table.size() + " món.");
                    Thread.sleep(500); //Tốc độ nấu: 0.5s/món
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        });

        // Luồng 2: Khách hàng (Lấy thức ăn từ bàn để ăn)
        Thread customer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    // Dùng lệnh take() (Nếu bàn trống, khách tự động ngồi chờ)
                    String food = table.take();
                    System.out.println("Khách hàng lấy: " + food + " để ăn.");
                    Thread.sleep(1500); //Tốc độ ăn: 1.5s/món
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        });

        // Bắt đầu
        chef.start();
        customer.start();
    }
}
