package javacoreadvance.chapter2_thread;

import java.util.Timer;
import java.util.TimerTask;

public class timerdemo {
    public static void main(String[] args){
        Timer timer = new Timer();
        TimerTask countdownTask = new TimerTask() {
            int count = 5;
            @Override
            public void run() {
                if(count > 0){
                    System.out.println("Đếm ngược: " + count);
                    count--;
                } else {
                    System.out.println("Kết thúc đếm ngược!");
                    timer.cancel();
                }
            }
        };

        System.out.println("Bắt đầu cài đặt đồng hồ...");
        timer.schedule(countdownTask, 0, 1000);
    }
}
