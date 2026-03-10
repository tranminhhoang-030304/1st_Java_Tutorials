package javacoreadvance.chapter2_thread;

//Khuyên dùng khởi tạo bằng implements Runnable (khuyên dùng)
class SimpleTask implements Runnable {
    private String taskName;
    public SimpleTask(String taskName){
        this.taskName = taskName;
    }

    @Override
    public void run(){
        System.out.println(" [" + taskName + "] Bắt đầu chạy ...");
        for(int i=1; i<=3; i++){
            System.out.println(" -> [" + taskName + "] Đang xử lý bước " + i);
            try{
                Thread.sleep(500); //timer_waiting
            } catch (InterruptedException e){
                System.out.println("[" + taskName + "] Bị gián đoạn!");
            }
        }
        System.out.println("[" + taskName + "] Đã hoàn thành và kết thúc");
    }
}

public class basicthreaddemo {
    public static void main(String[] args){
        System.out.println("Main Thread: Bắt đầu chương trình!");

        Thread t1 = new Thread(new SimpleTask("Fast Thread"));
        Thread t2 = new Thread(new SimpleTask("Slow Thread"));
        Thread t3 = new Thread(new SimpleTask("Normal Thread"));

        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        t3.setPriority(Thread.NORM_PRIORITY);

        System.out.println("Trạng thái t1 trước khi bắt đầu: " + t1.getState()); //New

        t1.start();
        t2.start();
        t3.start();

        System.out.println("Trạng thái t1 ngay sau khi bắt đầu: " + t1.getState()); //Runnable

        System.out.println("Main Thread: Đã chạy xong luồng chạy này!"); //terminated
    }
}
