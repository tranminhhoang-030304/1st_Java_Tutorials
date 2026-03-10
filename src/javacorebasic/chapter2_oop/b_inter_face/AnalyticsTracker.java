package javacorebasic.chapter2_oop.b_inter_face;

public interface AnalyticsTracker {
    void trackEvent(String eventName, String data);
}

//Interface là 1 tập hợp các phương thức trống (không có thân hàm)
//Dùng Interface khi muốn định nghĩa một khả năng/hành động (Runnable, Serializable, Payable) mà nhiều class không liên quan đến nhau cũng có thể làm được.
//Class nào implements (triển khai) một interface đều bắt buộc phải viết code chi tiết cho tất cả các hàm trong interface đó
//1 Class có thể triển khai nhiều Interface cùng lúc