//Xử lý trackingevents (wait và notify)
//Bối cảnh: Bạn có một luồng chuyên hứng dữ liệu sự kiện (như click chuột, đăng nhập) từ người dùng, và một luồng chuyên ghi các sự kiện đó xuống Database. Vì Database ghi chậm hơn tốc độ hứng, cần một bộ đệm (Buffer).
//Yêu cầu: Tạo một class EventBuffer chứa biến int eventCount = 0;
//1. Luồng 1 (GameClient): Chạy vòng lặp 5 lần để tạo event. Mỗi lần gọi hàm pushEvent(). Hàm này dùng synchronized. Nếu eventCount == 10 (đầy bộ đệm), luồng này phải wait(). Nếu chưa đầy thì tăng biến đếm lên, in ra "Đã nhận thêm 1 event. Tổng: X" và gọi notify()
//2. Luồng 2 (DatabaseWriter): Chạy vòng lặp để ghi event. Gọi hàm saveToDB(). Hàm này cũng synchronized. Nếu eventCount == 0 (không có gì để ghi), luồng này phải wait(). Nếu có, giảm biến đếm đi 1, in ra "Đã lưu 1 event vào DB. Còn lại: X" và gọi notify()
//3. Cho luồng Client chạy nhanh hơn (sleep 200), luồng Database ghi chậm hơn (sleep 800) để quan sát việc luồng Client phải chờ bộ đệm trống mới được đẩy tiếp

package javacoreadvance.chapter4_multithread.f_tasks;

public class task2 {
    public static void main(String[] args){
        System.out.println("HỆ THỐNG XỬ LÝ TRACKING EVENTS \n");
        System.out.println("Buffer size: 18 events");
        System.out.println("GameClient: tạo event mỗi 200ms");
        System.out.println("DBWriter: ghi DB mỗi 800ms");
        System.out.println("========================\n");
        EventBuffer buffer = new EventBuffer();
        Thread gameClient = new Thread(new GameClient(buffer), "GameClient-Thread");
        Thread dbWriter = new Thread(new DBWriter(buffer), "DBWriter-Thread");
        gameClient.start();
        dbWriter.start();
        try {
            gameClient.join();
            System.out.println("\n [Main] GameClient đã hoàn thành. Đợi DBWriter xử lý ...\n");
            Thread.sleep(2000);
            dbWriter.interrupt();
            dbWriter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n KẾT THÚC XỬ LÝ EVENTS");
    }
}
class EventBuffer {
    private int eventCount = 0;
    private final int MAX_CAPACITY = 18;
    public synchronized void pushEvent() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        while (eventCount >= MAX_CAPACITY) {
            System.out.println("[" + threadName + "] Buffer đầy (" + eventCount + "/" + MAX_CAPACITY + "). Chờ DB ghi...");
            wait();
            System.out.println("[" + threadName + "] Đã được đánh thức, tiếp tục gửi event!");
        }
        eventCount++;
        System.out.println(" [" + threadName + "] Đã nhận thêm 1 event. Tổng: " + eventCount + "/" + MAX_CAPACITY);
        notify();
        printBufferStatus();
    }
    public synchronized void saveToDB() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        while (eventCount == 0) {
            System.out.println(" [" + threadName + "] Buffer rỗng. Chờ event mới ...");
            wait();
            System.out.println(" [" + threadName + "] Đã được đánh thức, có event để ghi!");
        }
        eventCount--;
        System.out.println(" [" + threadName + "] Đã lưu 1 event vào DB. Còn lại: " + eventCount + "/" + MAX_CAPACITY);
        notify();
        printBufferStatus();
    }
    private void printBufferStatus(){
        StringBuilder bar = new StringBuilder("Buffer: [");
        for(int i=0; i< MAX_CAPACITY; i++){
            if(i < eventCount){
                bar.append("█ Event đang chờ xử lý...");
            } else {
                bar.append("░");
            }
        }
        bar.append("]").append(eventCount).append("/").append(MAX_CAPACITY);
        System.out.println(bar.toString());
    }
}
class GameClient implements Runnable {
    private final EventBuffer buffer;
    private static final int TOTAL_EVENTS = 5;
    public GameClient(EventBuffer buffer){
        this.buffer = buffer;
    }
    @Override
    public void run(){
        String threadName = Thread.currentThread().getName();
        try{
            for(int i=0; i<= TOTAL_EVENTS;i++) {
                System.out.println("\n[" + threadName + "] Đang tạo event #" + i);
                buffer.pushEvent();
                Thread.sleep(200);
            }
            System.out.println("\n[" + threadName + "] Đã tạo xong " + TOTAL_EVENTS + " events!");
        } catch (InterruptedException e){
            System.out.println("[" + threadName + "] Bị gián đoạn!");
            Thread.currentThread().interrupt();
        }
    }
}
class DBWriter implements Runnable {
    private final EventBuffer buffer;
    public DBWriter(EventBuffer buffer) {
        this.buffer = buffer;
    }
    @Override
    public void run(){
        String threadName = Thread.currentThread().getName();
        int eventsWritten = 0;
        try {
            while (!Thread.currentThread().isInterrupted()){
                buffer.saveToDB();
                eventsWritten++;
                Thread.sleep(800);
            }
        } catch (InterruptedException e) {
            System.out.println("\n [" + threadName + "] Đã ghi tổng cộng " + eventsWritten + " events!");
            System.out.println("[" + threadName + "] Kết thúc hoạt động!");
            Thread.currentThread().interrupt();
        }
    }
}

/**
1. wait() và notify()
 sequenceDiagram
 participant GC as GameClient (Producer)
 participant B as EventBuffer (Shared Resource)
 participant DB as DBWriter (Consumer)

 Note over GC: pushEvent()
 GC->>B: synchronized (khóa B)

 alt Buffer đầy
 B->>GC: wait() (giải phóng khóa)
 Note over GC: Chờ notify từ DB
 else Còn chỗ
 GC->>B: eventCount++
 B->>GC: notify()
 GC->>B: release lock
 end

 DB->>B: synchronized (khóa B)

 alt Buffer rỗng
 B->>DB: wait() (giải phóng khóa)
 Note over DB: Chờ notify từ GC
 else Có event
 DB->>B: eventCount--
 B->>DB: notify()
 DB->>B: release lock
 end

 2. Dùng while thay vì if
 // ĐÚNG - dùng while
 while (eventCount >= MAX_CAPACITY) {
 wait();
 }

 // SAI - dùng if
 if (eventCount >= MAX_CAPACITY) {
 wait();
 }

 +) Spurious wakeup: Thread có thể thức dậy mà không có notify()
 +) Kiểm tra lại điều kiện: Sau khi thức dậy, cần kiểm tra lại điều kiện
 +) An toàn: While đảm bảo kiểm tra lại điều kiện sau mỗi lần thức dậy

 3. Lưu ý:
 +) Synchronized methods: Đảm bảo chỉ một thread truy cập buffer tại một thời điểm
 +) wait(): Giải phóng lock và chờ được notify
 +) notify(): Đánh thức một thread đang wait (không giải phóng lock ngay)
 +) InterruptedException: Xử lý khi thread bị interrupt
 +) join(): Đợi thread kết thúc
 */