//Săn Voucher Flash Sale (Sử dụng ReentrantLock và tryLock)
//Bối cảnh: Chỉ còn duy nhất 1 mã giảm giá 99%. Có hàng nghìn người cùng ấn nút lấy mã cùng một tích tắc.
//Yêu cầu:
//1. Tạo class VoucherCampaign chứa biến int vouchers = 1; và một ReentrantLock
//2. Viết hàm sanMa(String tenUser). Trong hàm này
//+) Sử dụng tryLock(thời_gian_chờ, TimeUnit)
//+) Nếu lấy được khóa: Kiểm tra xem vouchers > 0 không. Nếu có, thông báo tenUser săn thành công và gán vouchers = 0. Nếu vouchers == 0, thông báo "Rất tiếc, mã đã bị người khác lấy". Cuối cùng nhớ unlock()
//+) Nếu KHÔNG lấy được khóa: In ra "Server đang quá tải, [tenUser] vui lòng thử lại sau!"
//3. Trong main, dùng vòng lặp tạo ra 10 Threads (đại diện cho 10 user) và cùng start() một lúc để chúng lao vào tranh giành lệnh sanMa()

package javacoreadvance.chapter4_multithread.f_tasks.task3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Bài tập 3: Săn Voucher Flash Sale với ReentrantLock và tryLock()
 *
 * Mô phỏng tình huống: 10 người cùng săn 1 voucher 99%
 * Sử dụng ReentrantLock để đồng bộ và tryLock() để tránh blocking vô thời hạn
 */
public class task3 {
    public static void main(String[] args) {
        System.out.println("Chương trình săn voucher flash sale \n");
        System.out.println("Duy nhất 1 voucher 99%!");
        System.out.println("Có 10 người cùng săn voucher...\n");
        VoucherCampaign campaign = new VoucherCampaign(1); // Tạo campaign với 1 voucher
        for(int i=1; i<=10; i++){ // Tạo và khởi chạy 10 thread đại diện cho 10 người dùng
            String userName = "User_" +i;
            Thread userThread = new Thread(new User(campaign, userName), "Thread-" + i);
            userThread.start();
        }
        try { // Đợi tất cả users hoàn thành
            Thread.sleep(5000); // Đợi 5 giây cho tất cả xử lý xong
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n Kết thúc chương trình!");
    }
}
/**
 * Lớp VoucherCampaign quản lý voucher và cơ chế khóa
 */
class VoucherCampaign {
    private int vouchers; // Số lượng voucher còn lại
    private final ReentrantLock lock = new ReentrantLock(); // Khóa để đồng bộ
    private static final int TIMEOUT = 100; // Thời gian chờ tối đa để lấy khóa (ms)
    public VoucherCampaign(int initialVouchers){
        this.vouchers = initialVouchers;
        System.out.println("Khởi tạo campaign với " + vouchers + "voucher \n");
    }
    /**
     * Phương thức săn voucher với cơ chế tryLock()
     * - Cố gắng lấy khóa trong thời gian TIMEOUT
     * - Nếu lấy được: kiểm tra và xử lý voucher
     * - Nếu không lấy được: báo quá tải
     */
    public void sanMa(String tenUser){
        String threadName = Thread.currentThread().getName();
        boolean isLockAcquired = false;
        try {
            System.out.println(" [" + tenUser + "] Đang săn voucher...");
            isLockAcquired = lock.tryLock(TIMEOUT, TimeUnit.MILLISECONDS); // Thử lấy lock trong 100ms
            if(isLockAcquired){ // ĐÃ LẤY ĐƯỢC KHÓA
                System.out.println(" [" + tenUser + "] Đã vào được hệ thống, đang kiểm tra voucher");
                xuLySanVoucher(tenUser); // Xử lý logic săn voucher
            } else { // KHÔNG LẤY ĐƯỢC KHÓA - Hệ thống quá tải
                System.out.println(" [" + tenUser + "] Server quá tải! Thử lại sau!");
            }
        } catch (InterruptedException e) {
            System.out.println("[" + tenUser + "] Bị gián đoạn trong quá trình săn voucher!");
            Thread.currentThread().interrupt();
        } finally { // Giải phóng khóa nếu đã lấy được
            if(isLockAcquired) {
                lock.unlock();
                System.out.println(" [" + tenUser + "] Đã rời khỏi hệ thống (unlock)");
            }
        }
    }
    /**
     * Xử lý logic săn voucher (chỉ được gọi khi đã có lock)
     */
    private void xuLySanVoucher(String tenUser) {
        if (vouchers > 0) { // Kiểm tra còn voucher không
            System.out.println(" [" + tenUser + "] Chúc mừng! Săn voucher 99% thành công!"); // Thành công! Lấy được voucher
            vouchers = 0; // Voucher đã hết
            System.out.println(" Trạng thái: Voucher đã hết!");
        } else { // Đã hết voucher
            System.out.println(" [" + tenUser + "] Rất tiếc, mã đã có chủ!");
        }
        try {  // Giả lập thời gian xử lý (100ms)
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    /**
     * Kiểm tra số lượng voucher còn lại (không đồng bộ - chỉ để debug)
     */
    public int getVouchers(){
        return vouchers;
    }
}
/**
 * Lớp User đại diện cho người dùng tham gia săn voucher
 */
class User implements Runnable {
    private final VoucherCampaign campaign;
    private final String userName;
    public User(VoucherCampaign campaign, String userName){
        this.campaign = campaign;
        this.userName = userName;
    }
    @Override
    public void run() { // Mỗi user sẽ cố gắng săn voucher
        campaign.sanMa(userName);
        if(campaign.getVouchers() == 0){ // In ra kết quả cuối cùng (để debug)
            System.out.println(" [" + userName + "] Voucher đã được săn hết!");
        }
    }
}

/**
 1. ReetrantLock
 private final ReentrantLock lock = new ReentrantLock();
 -) Có thể lock lại nhiều lần bởi cùng một thread
 -) Fairness: Có thể tạo lock công bằng (fair) hoặc không công bằng
 -) Ưu điểm so với 'synchronized'
    +) Có thể thử lock với thời gian chờ (tryLock)
    +) Có thể kiểm tra trạng thái lock
    +) Linh hoạt hơn trong việc quản lý lock
 2. tryLock()
 lock.tryLock(TIMEOUT, TimeUnit.MILLISECONDS)
 -) Không blocking vô thời hạn: Chỉ đợi tối đa TIMEOUT ms
 -) Trả về boolean: True nếu lấy được lock, false nếu timeout
 -) Xử lý quả tải: Nếu không lấy được lock -> Báo quá tải
 3. Luồng xử lý:
 graph TD
 A[Bắt đầu săn voucher] --> B{tryLock thành công?}
 B -->|Có| C[Vào critical section]
 B -->|Không| D[Báo quá tải - Kết thúc]

 C --> E{Còn voucher?}
 E -->|Có| F[🎉 CHÚC MỪNG! Lấy được voucher]
 E -->|Không| G[😢 Rất tiếc, đã hết]

 F --> H[unlock - Kết thúc]
 G --> H

 4. So sánh với 'synchronized'
 Đặc điểm           ||      synchronized             ||          ReetrantLock
 Cú pháp            ||    Đơn giản, tự động          ||       Phức tạp hơn, phải Unlock
 Thời gian chờ      ||      Không                    ||      tryLock() với timeout
 Kiểm tra lock      ||      Không                    ||       isLocked(), getQueueLength()
 Fairness           ||      Không                    ||        Có thể cấu hình
 Interrupt          ||      Không                    ||        lockInterruptibly()

 5. Chú ý:
 -) Luôn unlock trong finally: Tránh deadlock nếu có exception
 -) tryLock với timeout: Tránh thread chờ vô thời hạn
 -) Kiểm tra điều kiện sau khi có lock: Voucher có thể đã hết trong khi chờ
 -) Xử lý InterruptedException: Khi thread bị interrupt trong lúc chờ lock
 */