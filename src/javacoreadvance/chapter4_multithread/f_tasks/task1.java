//Hệ thống TMDT (BlockingQueue và ThreadPool)
//Xây dựng hệ thống xử lý luồng đơn hàng (Order) cho một sàn thương mại điện tử trong ngày Flash Sale. Khách hàng bấm mua liên tục, nếu xử lý từng đơn một thì server sẽ sập.
//Yêu cầu:
//1. Khởi tạo một LinkedBlockingQueue<String> orderQueue làm hàng đợi đơn hàng.
//2. Dùng một vòng lặp for (chạy trên Main Thread) để giả lập việc nhận 20 đơn hàng liên tục (Nhét chuỗi "Đơn hàng #1", "Đơn hàng #2"... vào orderQueue)
//3. Khởi tạo một ExecutorService (Thread Pool) với 3 luồng (đại diện cho 3 máy chủ xử lý)
//4. Các luồng trong Pool sẽ liên tục take() đơn hàng từ Queue ra để xử lý (in ra màn hình: "Máy chủ [Tên_Luồng] đang đóng gói [Tên_Đơn_Hàng]") và sleep(500) để giả lập thời gian đóng gói.
//5. Đảm báo đóng gói ThreadPool sau khi gọi xong

package javacoreadvance.chapter4_multithread.f_tasks;

public class task1 {
}
