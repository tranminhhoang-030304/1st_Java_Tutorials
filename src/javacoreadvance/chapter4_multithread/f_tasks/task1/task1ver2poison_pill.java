package javacoreadvance.chapter4_multithread.f_tasks.task1;

import java.util.concurrent.*;
import java.util.concurrent.ThreadLocalRandom; //Mỗi luồng riêng 1 máy random; Math.random() là cả hệ thống dùng chung 1 máy random

public class task1ver2poison_pill {
    private static final LinkedBlockingQueue<String> orderQueue = new LinkedBlockingQueue<>(); // Hàng đợi đơn hàng - Thread-safe, có thể blocking
    private static final int TOTAL_ORDERS = 20; // Số lượng đơn hàng cần xử lý
    private static final int NUMBER_OF_SERVICES = 3; // Số lượng luồng (thread) xử lý
    private static final String POISON_PILL = "STOP_SERVER"; // Thêm poison_pill

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
                        if(order.equals(POISON_PILL)){
                            System.out.println("Máy chủ " + serverId + " (" + threadName + ") nhận lệnh nghỉ. Đóng máy!");
                            break; //Dừng vòng lặp và luồng tự động kết thúc
                        }
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
            System.out.printf("   -> [%s] %s: Kho OK, đang xuất kho...%n", threadName, order);
            Thread.sleep(100); // Thời gian lấy hàng

            // Bước 2: Tính toán phí ship dựa trên khoảng cách (Random từ 15k đến 35k)
            int shippingFee = ThreadLocalRandom.current().nextInt(15000, 35001);
            System.out.printf("   -> [%s] %s: Đã tính phí ship là %,d VNĐ.%n", threadName, order, shippingFee);

            // Bước 3: Đóng gói (Logic cốt lõi)
            System.out.printf("   -> [%s] %s: Đang đóng gói dán tem...%n", threadName, order);
            Thread.sleep(500); // Cân nặng nhất, tốn 0.5 giây

            // Bước 4: Gửi Email / SMS xác nhận cho khách hàng
            System.out.printf("   -> [%s] %s: Đã gửi Email xác nhận thành công.%n", threadName, order);

            // Hoàn thành
            System.out.printf("[%s] ✅ Máy chủ %d HOÀN THÀNH %s%n", threadName, serverId, order);

        } catch (InterruptedException e) {
            // LỖI 1: Lỗi luồng hệ thống (Bị giật điện shutdownNow)
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
        System.out.println("Dùng " + NUMBER_OF_SERVICES + " poison_pill vào cuối queue...");
        for(int i=0; i< NUMBER_OF_SERVICES; i++){
            orderQueue.offer(POISON_PILL);
        }
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
                executorService.shutdown(); //executorService.shutdown() là đủ vì các luồng tự biết cách dừng, không cần shutdownNow() nữa
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
 Các cách dừng Thread:
 +) Cách 1: Poison Pill (an toàn cao)
    -) Cơ chế: Gửi 1 tín hiệu đặc biệt vào hàng đợi dữ liệu
    -) Ưu: Luồng dừng nhẹ nhàng, đảm bảo mọi dữ liệu đứng trước poison pill đều được xử lý xong 100% và không có Exception nào được trả ra
    -) Nhược: Phải thả số poison pill = số luồng (3 luồng mà chỉ thả 2 poison pill thì 1 luồng vẫn kẹt)
    -) Khi nào dùng: Luôn ưu tiên dùng khi kiến trúc dùng mô hình Producer-Consumer thông qua 'BlockingQueue'
 +) Cách 2: Lệnh shutdown() + awaitTermination() + shutdownNow()
    -) Cơ chế: Ra lệnh ngắt từ bộ điều khiển trung tâm (Thread Pool)
    -) Ưu điểm: Có thể dùng cho mọi Thread Pool, xử lý được cả trường hợp luồng bị kẹt vô hạn không lối thoát (nhờ shutdownNow() ép văng ra InterruptedException).
    -) Nhược điểm: shutdownNow() dừng ngay lập tức nên nếu luồng đang dở tay ghi database mà bị buộc dừng, dữ liệu có thể bị hỏng (corrupted) hoặc chỉ ghi được một nửa
    -) Khi nào dùng: Khi quản lý một mảng công việc phức tạp, gọi API bên ngoài, hoặc khi bắt buộc hệ thống phải đóng cửa trong thời gian quy định
 +) Cách 3: Dùng cờ báo hiệu (Volatile Boolean Flag) - Dành cho luồng đơn giản
    -) Cơ chế: Tạo một biến private volatile boolean isRunning = true;. Trong luồng, thay vì while(true), viết while(isRunning). Khi muốn dừng, luồng Main chỉ cần đổi isRunning = false;
    -) Ưu điểm: Dễ hiểu, cực kỳ dễ cài đặt
    -) Nhược điểm: Vô dụng nếu luồng đang bị Blocked (Ví dụ: luồng đang gọi orderQueue.take() hoặc Thread.sleep()). Dù cờ đã đổi thành false, nhưng luồng đang ngủ thì không thể kiểm tra cờ được.
    -) Khi nào dùng: Khi luồng chạy một công việc tính toán liên tục, không có các thao tác gây "ngủ" (Blocking I/O, sleep, wait)
=> KẾT LUẬN:
    1. Code có BlockingQueue? -> Dùng Poison Pill
    2. Code dùng ExecutorService xử lý đa tác vụ lung tung? -> Dùng awaitTermination + shutdownNow().
    3. Code 1 luồng chạy tính toán liên tục? -> Dùng cờ Volatile Boolean.
  */