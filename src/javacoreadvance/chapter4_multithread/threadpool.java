package javacoreadvance.chapter4_multithread;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

class Task implements Runnable {
    private int taskId;
    public Task(int id) {
        this.taskId = id;
    }
    @Override
    public void run(){
        System.out.println("Luồng [" + Thread.currentThread().getName() + "] đang thực hiện Task " + taskId);
        try {
            Thread.sleep(1000);//giả lập 1s làm việc
        } catch (InterruptedException e) {}
    }
}

public class threadpool{
    public static void main(String[] args){
        ExecutorService pool = Executors.newFixedThreadPool(3); //tạo 1 pool có 3 luồng
        System.out.println("Cần thực hiện 10 task...");
        for (int i = 1; i <= 10; i++){
            pool.execute(new Task(i)); //đẩy task vào pool
        }
        pool.shutdown(); //làm hết task thì dừng, k nhận thêm
    }
}

