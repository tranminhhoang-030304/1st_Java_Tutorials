package javacoreadvance.chapter4_multithread;

import java.util.concurrent.TimeUnit; // Thêm thư viện đo thời gian
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class e_lock_unlock_trylock {
    private int count = 0;
    private final Lock lock = new ReentrantLock();

    public void tangbiendem() {
        try {
            // Thay vì bỏ ngay, sẽ chờ tối đa 200 ms
            if (lock.tryLock(200, TimeUnit.MILLISECONDS)) {
                try {
                    count++;
                    System.out.println("🟢 " + Thread.currentThread().getName() + " -> ĐÃ VÀO PHÒNG! Đếm: " + count);
                    Thread.sleep(100); // Thời gian làm việc trong phòng: 100ms
                } finally {
                    lock.unlock(); // Xong việc phải trả lại khóa
                    System.out.println("   🔓 " + Thread.currentThread().getName() + " -> Đã đi ra.");
                }
            } else {
                // Chỉ chạy vào đây nếu đã đứng chờ hết 200ms mà cửa vẫn không mở
                System.out.println("🔴 " + Thread.currentThread().getName() + " -> Đứng đợi 200ms, bỏ cuộc!");
            }
        } catch (InterruptedException e) {
            // tryLock có thời gian bắt buộc phải bắt lỗi InterruptedException (đang đứng chờ bị ai đó ngắt ngang)
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        e_lock_unlock_trylock doiTuongChung = new e_lock_unlock_trylock();

        // Tạo 3 luồng cùng tranh nhau
        Thread t1 = new Thread(() -> { for(int i=0; i<3; i++) doiTuongChung.tangbiendem(); }, "Luồng A");
        Thread t2 = new Thread(() -> { for(int i=0; i<3; i++) doiTuongChung.tangbiendem(); }, "Luồng B");
        Thread t3 = new Thread(() -> { for(int i=0; i<3; i++) doiTuongChung.tangbiendem(); }, "Luồng C");

        t1.start(); t2.start(); t3.start();
    }
}