package javacoreadvance.chapter4_multithread.f_tasks.task3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class task3_CountDownLatch {
    public static void main(String[] args){
        System.out.println("Chương trình săn voucher flash sale (CountDownLatch)\n");
        VoucherCampaign campaign = new VoucherCampaign(1);
        int numberOfUsers = 10;
        CountDownLatch latch = new CountDownLatch(numberOfUsers);

        for (int i = 1; i <= numberOfUsers; i++){ // Đổi i=0 thành i=1 để in ra từ User_1 đến 10
            String userName = "User_" + i;
            // Đã bổ sung biến latch vào đây
            Thread userThread = new Thread(new User(campaign, userName, latch), "Thread-" + i);
            userThread.start();
        }

        try {
            System.out.println("Đang chờ các users săn mã...");
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n Đã xong! Kết thúc chương trình!");
    }

    // Đưa class vào trong để tránh trùng lặp
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
    } // ĐÃ BỔ SUNG NGOẶC ĐÓNG CỦA VoucherCampaign Ở ĐÂY!

    static class User implements Runnable {
        private final VoucherCampaign campaign;
        private final String userName;
        private final CountDownLatch latch;

        public User(VoucherCampaign campaign, String userName, CountDownLatch latch){
            this.campaign = campaign;
            this.userName = userName;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                campaign.sanMa(userName);
                if(campaign.getVouchers() == 0){
                    System.out.println(" [" + userName + "] Voucher đã được săn hết!");
                }
            } finally {
                latch.countDown();
            }
        }
    }
}