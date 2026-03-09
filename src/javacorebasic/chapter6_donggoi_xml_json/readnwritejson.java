package javacorebasic.chapter6_donggoi_xml_json;

import com.google.gson.Gson;
// Tạo một "khuôn" dữ liệu giống với các thông số tracking
class TrackingEvent {
    String eventName;
    String deviceId;
    double revenue;

    public TrackingEvent(String eventName, String deviceId, double revenue) {
        this.eventName = eventName;
        this.deviceId = deviceId;
        this.revenue = revenue;
    }
}

public class readnwritejson {
    public static void main(String[] args) {
        Gson gson = new Gson();

        // 1. GHI JSON (Chuyển Object Java -> Chuỗi JSON)
        TrackingEvent myEvent = new TrackingEvent("app_install", "Device-X99", 0.0);
        String jsonResult = gson.toJson(myEvent);

        System.out.println("--- DỮ LIỆU ĐÃ ĐÓNG GÓI THÀNH JSON ---");
        System.out.println(jsonResult);
        // Kết quả in ra: {"eventName":"app_install","deviceId":"Device-X99","revenue":0.0}

        // 2. ĐỌC JSON (Chuyển Chuỗi JSON -> Object Java)
        String incomingJson = "{\"eventName\":\"in_app_purchase\",\"deviceId\":\"Device-A100\",\"revenue\":49.99}";

        TrackingEvent parsedEvent = gson.fromJson(incomingJson, TrackingEvent.class);

        System.out.println("\n--- DỮ LIỆU ĐÃ ĐỌC TỪ JSON ---");
        System.out.println("Tên sự kiện: " + parsedEvent.eventName);
        System.out.println("Doanh thu: $" + parsedEvent.revenue);
    }
}
