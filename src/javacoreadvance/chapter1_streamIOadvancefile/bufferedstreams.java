package javacoreadvance.chapter1_streamIOadvancefile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class bufferedstreams {
    public static void main(String[] args){
        int records = 1_000_000;
        String eventData = "{\"event_name\": \"level_up\", \"user_id\": 1024, \"timestamp\": 1710034500}\n";
        System.out.println("Ghi 1 triệu dòng data ...");

        //Cách 1: Basic I/O
        long startTime1 = System.currentTimeMillis();
        try (FileWriter fw = new FileWriter("basic_tracking_log.txt")){
            for (int i = 0; i< records; i++){
                fw.write(eventData);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("Basic I/O mất: " + (endTime1 - startTime1) + " ms");

        //Cách 2: BufferedWriter
        long startTime2 = System.currentTimeMillis();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("buffered_tracking_log.txt"), 8192)) {
            for (int i=0; i<records; i++){
                bw.write(eventData); //lưu vào Ram, đầy Ram mới đẩy xuống ổ cứng
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("Buffered I/O mất: " + (endTime2 - startTime2) + " ms");
    }
}
