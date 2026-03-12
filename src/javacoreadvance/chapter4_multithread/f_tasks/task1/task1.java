//Hệ thống TMDT (BlockingQueue và ThreadPool)
//Xây dựng hệ thống xử lý luồng đơn hàng (Order) cho một sàn thương mại điện tử trong ngày Flash Sale. Khách hàng bấm mua liên tục, nếu xử lý từng đơn một thì server sẽ sập.
//Yêu cầu:
//1. Khởi tạo một LinkedBlockingQueue<String> orderQueue làm hàng đợi đơn hàng.
//2. Dùng một vòng lặp for (chạy trên Main Thread) để giả lập việc nhận 20 đơn hàng liên tục (Nhét chuỗi "Đơn hàng #1", "Đơn hàng #2"... vào orderQueue)
//3. Khởi tạo một ExecutorService (Thread Pool) với 3 luồng (đại diện cho 3 máy chủ xử lý)
//4. Các luồng trong Pool sẽ liên tục take() đơn hàng từ Queue ra để xử lý (in ra màn hình: "Máy chủ [Tên_Luồng] đang đóng gói [Tên_Đơn_Hàng]") và sleep(500) để giả lập thời gian đóng gói.
//5. Đảm báo đóng gói ThreadPool sau khi gọi xong

package javacoreadvance.chapter4_multithread.f_tasks.task1;

import java.util.concurrent.*;
import java.util.concurrent.ThreadLocalRandom; //Mỗi luồng riêng 1 máy random; Math.random() là cả hệ thống dùng chung 1 máy random

public class task1 {
    private static final LinkedBlockingQueue<String> orderQueue = new LinkedBlockingQueue<>(); // Hàng đợi đơn hàng - Thread-safe, có thể blocking
    private static final int TOTAL_ORDERS = 20; // Số lượng đơn hàng cần xử lý
    private static final int NUMBER_OF_SERVICES = 3; // Số lượng luồng (thread) xử lý
    public static void main(String[] args){
        System.out.println("Hệ thống xử lý đơn hàng Flash Sale \n");
        System.out.println("Bắt đầu nhận đợn hàng từ khách hàng!");
        ExecutorService orderProcessors = Executors.newFixedThreadPool(NUMBER_OF_SERVICES); // Bước 1: Tạo Thread Pool với 3 máy chủ xử lý
        try{
            startOrderProcessors(orderProcessors); // Bước 2: Khởi động các máy chủ xử lý đơn hàng
            simulateIncomingOrders(); // Bước 3: Giả lập nhận đơn hàng từ khách hàng
            Thread.sleep(5000);  // Đợi một chút để đảm bảo tất cả đơn hàng được xử lý
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Hệ thống bị gián đoạn: " + e.getMessage());
        } finally {
            shutdownOrderProcessors(orderProcessors); // Bước 4: Đóng Thread Pool
        }
        System.out.println("\n Kết thúc xử lý đơn hàng!");
    }
    /**
     * Khởi động các máy chủ xử lý đơn hàng
     * Mỗi máy chủ là một thread liên tục lấy đơn hàng từ queue để xử lý
     */
    private static void startOrderProcessors(ExecutorService executorService){
        System.out.println("Khởi động " + NUMBER_OF_SERVICES + " máy chủ xử lý đơn hàng ...\n");
        for (int i=1; i<= NUMBER_OF_SERVICES; i++){
            final int serverId = i;
            executorService.submit(() -> {
                String threadName = Thread.currentThread().getName(); // Lấy tên của thread hiện tại
                try {
                    while (true){
                        String order = orderQueue.take(); // take() sẽ block nếu queue rỗng
                        processOrder(serverId, threadName, order); // Xử lý đơn hàng
                    }
                } catch (InterruptedException e) {
                    System.out.println("Máy chủ " + serverId + " (" + threadName + ") đã dừng hoạt động"); // Thread bị interrupt khi shutdown
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
    /**
     * Xử lý một đơn hàng
     * - In thông tin máy chủ đang xử lý
     * - Sleep giả lập thời gian đóng gói
     */
    private static void processOrder(int serverId, String threadName, String order) {
        System.out.printf("[%s] Máy chủ %d đang đóng gói %s%n", threadName, serverId, order);
        try {
/**
 * Khối 'try' chứa toàn bộ quy trình:
 * Nếu đang ở 'bước 1' mà kho báo hết hàng (throw new RuntimeException) thì hệ thống sẽ gọi thẳng khối catch(RuntimeException) bên dưới
 * => Bỏ qua tự động các bước 2,3,4, nâng cao hiệu quả xử lý
 */
            // Bước 1: Kiểm tra tồn kho
            // Dùng ThreadLocalRandom thay cho Math.random() để tối ưu hiệu năng và an toàn cho Đa luồng
            boolean inStock = ThreadLocalRandom.current().nextInt(100) >= 15; // Tỷ lệ 85% là còn hàng

            if (!inStock) {
                // Giả lập rủi ro: Khách đặt nhanh quá nhưng kho thực tế đã hết
                throw new RuntimeException("Kho báo hết hàng đột xuất!");
            }
            System.out.printf("   -> [%s] %s: Kho còn hàng, đang xuất kho...%n", threadName, order);
            Thread.sleep(100); // Thời gian lấy hàng

            // Bước 2: Tính toán phí ship dựa trên khoảng cách (Random từ 15k đến 55k)
            int shippingFee = ThreadLocalRandom.current().nextInt(15000, 55001);
            System.out.printf("   -> [%s] %s: Đã tính phí ship là %,d VNĐ.%n", threadName, order, shippingFee);

            // Bước 3: Đóng gói (Logic cốt lõi)
            System.out.printf("   -> [%s] %s: Đang đóng gói dán tem...%n", threadName, order);
            Thread.sleep(500); // Cân nặng nhất, tốn 0.5 giây

            // Bước 4: Gửi Email / SMS xác nhận cho khách hàng
            System.out.printf("   -> [%s] %s: Đã gửi Email xác nhận thành công.%n", threadName, order);

            // Hoàn thành
            System.out.printf("[%s] ✅ Máy chủ %d HOÀN THÀNH %s%n", threadName, serverId, order);

        } catch (InterruptedException e) {
            // LỖI 1: Lỗi luồng hệ thống (Bị shutdownNow)
            // Phải khôi phục trạng thái ngắt (interrupt status) và báo cáo
            Thread.currentThread().interrupt();
            System.err.printf("❌ [%s] Quá trình xử lý %s BỊ GIÁN ĐOẠN KHẨN CẤP do đóng Server!%n", threadName, order);
/**
 ** Tách biệt 2 khối catch:
 * Lỗi InterruptedException là do Server gọi lệnh ngắt
 * Lỗi RuntimeException là lỗi do hết hàng. Khi bắt lỗi ở đây, luồng in ra cảnh báo xong thì hàm kết thúc. Luồng sẽ quay lại vòng lặp while(true) và lấy đơn hàng tiếp theo ra xử lý mà không hề bị chết hay văng app
 */
        } catch (RuntimeException e) {
            // LỖI 2: Lỗi nghiệp vụ (Business Error)
            // Lỗi này KHÔNG làm chết luồng. Luồng in ra lỗi, hủy đơn, rồi vẫn sống nhăn răng đi lấy đơn khác làm tiếp.
            System.err.printf("⚠️ [%s] HỦY %s: %s (Đang tiến hành hoàn tiền cho khách)%n", threadName, order, e.getMessage());
        }
    }
    /**
     * Giả lập việc nhận đơn hàng liên tục từ khách hàng
     * Các đơn hàng được đưa vào queue ngay khi nhận được
     */
    private static void simulateIncomingOrders() throws InterruptedException{
        System.out.println("Bắt đầu nhận 20 đơn hàng từ khách hàng...\n");
        for(int i=1; i<=TOTAL_ORDERS; i++){
            String order = "Đơn hàng #" + i;
            orderQueue.offer(order); // Đưa đơn hàng vào queue
            System.out.println("Đã nhận: " + order);
            Thread.sleep(200); // Giả lập thời gian giữa các đơn hàng (khách hàng bấm mua liên tục)
        }
        System.out.println("\nĐã nhận xong: " + TOTAL_ORDERS + " đơn hàng!\n");
    }
    /**
     * Đóng Thread Pool gọn gàng
     * - Không nhận thêm tác vụ mới
     * - Đợi các tác vụ hiện tại hoàn thành
     * - Force shutdown nếu quá thời gian chờ
     */
    private static void shutdownOrderProcessors(ExecutorService executorService) {
        System.out.println("\nĐang đóng các máy chủ xử lý đơn hàng ...");
        executorService.shutdown(); // Không nhận thêm tác vụ mới
        try{
            if(!executorService.awaitTermination(5, TimeUnit.SECONDS)){ // Đợi tối đa 5 giây cho các tác vụ hiện tại hoàn thành
                System.out.println("Một số máy chủ chưa xử lý xong, buộc dừng...");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Quá trình đóng máy chủ bị gián đoạn");
            executorService.shutdown();
            Thread.currentThread().interrupt();
        }
        System.out.println("Đã đóng tất cả máy chủ thành công!");
    }
}

/**
 * Giải thích chi tiết
 *
 * 1. LinkedBlockingQueue (hàng đợi liên kết)
 * + Thread-safe: Nhiều thread có thể cùng lúc thêm/lấy phần tử an toàn
 * + Blocking operations: take() sẽ tự động block nếu queue rỗng
 * + Không giới hạn kích thước: Có thể chứa vô hạn số lượng đơn hàng (phù hợp với Flash Sale)
 *
 * 2. ExecutorService (Thread Pool)
 * +) FixedThreadPool(3): Tạo Pool với 3 thread cố định
 * +) Tái sử dụng thread: Các thread được reuse để xử lý nhiều đơn
 * +) Vòng đời: Tự động quản lý việc tạo/destroy thread
 *
 * 3. Cơ chế hoạt động
 * 3.1. graph LR
 *     A[Khách hàng] -->|Đặt hàng| B[Order Queue]
 *     B -->|take| C[Máy chủ 1]
 *     B -->|take| D[Máy chủ 2]
 *     B -->|take| E[Máy chủ 3]
 * 3.2. Producer (main thread)
 * +) Tạo 20 đơn hàng, mỗi 200ms thêm 1 đơn vào queue
 * +) Không cần quan tâm ai xử lý, chỉ cần đưa vào queue
 * 3.3. Consumer (3 thread)
 * +) Luôn lấy đơn hàng từ queue
 * +) Nếu queue rỗng, thread tự động ngủ (block)
 * +) Khi có đơn mới, thread được đánh thức và xử lý
 * 3.4. Xử lý song song
 * +) 3 đơn được xử lý đồng thời
 * +) Mỗi đơn mất 0.5s để đóng gói
 * 4. Ưu điểm
 * +) Load balancing tự động: Các đơn hàng được phân phối đều cho các máy chủ
 * +) Không bị quá tải: Queue hấp thụ các đơn hàng đến nhanh
 * +) Tiết kiệm tài nguyên: Thread pool giới hạn số thread đồng thời
 * +) Dễ mở rộng: Chỉ cần thay đổi số lượng máy chủ trong pool
 */