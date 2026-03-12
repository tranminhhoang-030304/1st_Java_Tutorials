package javacoreadvance.chapter4_multithread.f_tasks.task3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class task3_ExecutorService {
    public static void main(String[] args){
        System.out.println("Chương trình săn voucher flash sale (ExecutorService)");
        VoucherCampaign campaign = new VoucherCampaign(1);
        int numberOfUsers = 10;
        ExecutorService pool = Executors.newFixedThreadPool(numberOfUsers);

        for(int i = 1; i<= numberOfUsers; i++){
            String userName = "User_" + i;
            pool.execute(new User(campaign, userName));
        }
        pool.shutdown();

        try {
            System.out.println("Đang chờ các luồng giao dịch hoàn tất...");
            if(!pool.awaitTermination(5, TimeUnit.SECONDS)){
                System.out.println("Quá thời gian, hệ thống sẽ dừng!");
                pool.shutdownNow();
            }
        } catch (InterruptedException e){
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("\n Chương trình kết thúc!");
    }

    static class VoucherCampaign {
        private int vouchers;
        private final ReentrantLock lock = new ReentrantLock();
        private static final int TIMEOUT = 100;

        public VoucherCampaign(int initialVouchers){
            this.vouchers = initialVouchers;
            System.out.println("Khởi tạo campaign với " + vouchers + " voucher \n");
        }

        public void sanMa(String tenUser){
            boolean isLockAcquired = false;
            try {
                System.out.println(" [" + tenUser + "] Đang săn voucher...");
                isLockAcquired = lock.tryLock(TIMEOUT, TimeUnit.MILLISECONDS);
                if(isLockAcquired){
                    System.out.println(" [" + tenUser + "] Đã vào được hệ thống, đang kiểm tra voucher");
                    xuLySanVoucher(tenUser);
                } else {
                    System.out.println(" [" + tenUser + "] Server quá tải! Thử lại sau!");
                }
            } catch (InterruptedException e) {
                System.out.println("[" + tenUser + "] Bị gián đoạn trong quá trình săn voucher!");
                Thread.currentThread().interrupt();
            } finally {
                if(isLockAcquired) {
                    lock.unlock();
                    System.out.println(" [" + tenUser + "] Đã rời khỏi hệ thống (unlock)");
                }
            }
        }

        private void xuLySanVoucher(String tenUser) {
            if (vouchers > 0) {
                System.out.println(" [" + tenUser + "] Chúc mừng! Săn voucher 99% thành công!");
                vouchers = 0;
                System.out.println(" Trạng thái: Voucher đã hết!");
            } else {
                System.out.println(" [" + tenUser + "] Rất tiếc, mã đã có chủ!");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public int getVouchers(){
            return vouchers;
        }
    }

    static class User implements Runnable {
        private final VoucherCampaign campaign;
        private final String userName;

        public User(VoucherCampaign campaign, String userName){
            this.campaign = campaign;
            this.userName = userName;
        }

        @Override
        public void run(){
            campaign.sanMa(userName);
            if(campaign.getVouchers() == 0) {
                System.out.println(" [" + userName + "] Voucher đã được săn hết!");
            }
        }
    }
}