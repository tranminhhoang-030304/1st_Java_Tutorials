package javacoreadvance.chapter4_multithread;

public class e_synchronized_dongbo {
    int sodu = 100; //gắn synchronized để chỉ 1 người rút tiền 1
    public synchronized void ruttien(String ten, int sotienrut){
        if (sodu >= sotienrut){
            System.out.println(ten + " tài khoản đủ tiền, chuẩn bị rút ...");
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            sodu -= sotienrut;
            System.out.println(ten + " rút tiền thành công! số dư còn: " + sodu);
        } else {
            System.out.println(ten + " rút tiền không thành công! hãy thử lại!");
        }
    }
}
