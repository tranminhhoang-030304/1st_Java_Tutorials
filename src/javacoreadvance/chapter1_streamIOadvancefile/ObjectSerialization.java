package javacoreadvance.chapter1_streamIOadvancefile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class TrackingEvent implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L; //phiên bản, sau này thay đổi cấu trúc đổi số để máy biết phiên bản cũ
    String eventName;
    double revenue;
    transient String sessionId; //Không lưu trường này vào file

    public TrackingEvent(String eventNam, double revenue, String sessionId){
        this.eventName = eventNam;
        this.revenue = revenue;
        this.sessionId = sessionId;
    }

    @Override
    public String toString(){
        return "Event: " + eventName + " || Doanh thu: " + revenue + " || Session: " + sessionId;
    }
}

public class ObjectSerialization {
    public static void main (String[] args){
        List<TrackingEvent> events = new ArrayList<>();
        events.add(new TrackingEvent("app_install", 0.0, "sess_abc123"));
        events.add(new TrackingEvent("in_app_purchase", 4.99, "sess_xyz789"));
        String filePath = "events_backup.dat";
        //Ghi Object xuống file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))){
            oos.writeObject(events);
            System.out.println("Đã đóng băng và lưu list object");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Đọc object từ dưới file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))){
            List<TrackingEvent> restoreEvents = (List<TrackingEvent>) ois.readObject(); //ép kiểu về định dạng ban đầu
            System.out.println("Dữ liệu sau khi rã đông:");
            for(TrackingEvent ev : restoreEvents){
                System.out.println(ev);
            } //trường sessionId in ra là Null vì đã được đánh dấu ở trên
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
