package javacorebasic.chapter2_oop.b_inter_face;

public class AppMetricaTracker implements AnalyticsTracker {
    @Override
    public void trackEvent(String eventName, String data) {
        //các hàm viết ở đây...
        System.out.println("Gửi event [" + eventName + "] lên hệ thống với data: " + data);
    }
}
//Class thực tế tuân thủ quy tắc của Interface