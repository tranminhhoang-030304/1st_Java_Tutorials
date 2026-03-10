package javacoreadvance.chapter4_multithread;

public class b_deadlockdemo {
    public static void main(String[] args) {
        final String key_A = "tài liệu";
        final String key_B = "con dấu";

        Thread t1 = new Thread(() -> { //luồng 1
            synchronized (key_A) { //luồng 1 giữ tài liệu
                System.out.println("Đã cầm tài liệu, đang chờ con dấu");
                try {
                    Thread.sleep(100); //chờ 0,1s để lấy con dấu
                } catch (Exception e) {

                }
                synchronized (key_B) { //lấy được con dấu
                    System.out.println("Đã cầm cả con dấu!");
                }
            }
        });

        Thread t2 = new Thread(() -> { //luồng 2
            synchronized (key_B) { //luồng 2 giữ con dấu
                System.out.println("Đã cầm con dấu, đang chờ tài liệu!");
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
                synchronized (key_A) { //lấy được tài liệu
                    System.out.println("Đã có cả tài liệu!");
                }
            }
        });

        t1.start();
        t2.start();
        System.out.println("Main Thread: Java Programme Simulation DeadLock!");
    }
}
