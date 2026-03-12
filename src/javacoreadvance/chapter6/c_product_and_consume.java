/**
 - Có 1 MQ chứa các message, cấu trúc message tùy chọn, MQ có size giới hạn
 - Có 1 luồng producer: định kì tạo ra 1 message và đưa vào MQ, MQ full thì luồng sẽ đợi đến khi MQ hết full (có slot) và tiếp tục tạo message mới để đưa vào MQ
 - Có 1 luồng consumer: mỗi lần lấy ra 1 message từ MQ và in message ra màn hình, nếu MQ trống thì sẽ đợi cho đến khi có message trong MQ để xử lý
 */
/**
 Dùng ArrayBlockingQueue thay vì phải viết hàm wait() và notify() thủ công
 +) Lệnh queue.put(): Tự động wait() nếu hàng đợi đầy
 +) Lệnh queue.take(): TỰ động wait() nếu hàng đợi rỗng
 */
package javacoreadvance.chapter6;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class c_product_and_consume {
    public static void main(String[] args){ // Tạo Message Queue giới hạn đúng 5 tin nhắn
        BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(5);
        Thread producer = new Thread(() -> { // Luồng Sản xuất (Producer)
            try {
                for(int count = 1; count <= 20; count ++) {
                    String msg = "Mesage-" + count;
                    System.out.println("Producer đang tạo và đẩy: " + msg);
                    messageQueue.put(msg); // NẾU FULL: Tự động đứng chờ ở đây
                    Thread.sleep(300); // Tốc độ sản xuất: 0.3s/tin (Nhanh hơn tiêu thụ)
                }
                System.out.println("Đã đủ số lượng. Dừng tạo message!");
                messageQueue.put("Stop!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread consumer =  new Thread(() -> { // Luồng Tiêu thụ (Consumer)
            try {
                while (true) {
                    String msg = messageQueue.take(); // NẾU EMPTY: Tự động đứng chờ ở đây
                    if("Stop!".equals(msg)){
                        System.out.println("Nhận tín hiệu Stop! Dừng hoạt động!");
                        break;
                    }
                    System.out.println(" -> Consumer đã lấy và xử lý: " + msg);
                    Thread.sleep(1000); // Tốc độ tiêu thụ: 1s/tin (Chậm hơn sản xuất)
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        producer.start();
        consumer.start();
    }
}
