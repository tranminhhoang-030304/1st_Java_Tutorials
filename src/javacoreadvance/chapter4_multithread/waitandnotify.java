package javacoreadvance.chapter4_multithread;

class BankAccount {
    int balance = 0;

    public synchronized void withdraw(int amount) {
        System.out.println("Đang rút " + amount + "...");
        while (balance < amount) {
            System.out.println("Tài khoản hết tiền! Đứng chờ...");
            try {
                wait(); // Ngủ và nhả khóa ra cho luồng Nạp Tiền có thể đi vào
            } catch (InterruptedException e) {}
        }
        balance -= amount;
        System.out.println("Rút thành công " + amount + "! Còn lại: " + balance);
    }

    public synchronized void deposit(int amount) {
        System.out.println("Đang nạp " + amount + "...");
        balance += amount;
        System.out.println("Nạp xong! Gọi người đang chờ dậy.");
        notify(); // Đánh thức 1 luồng đang gọi wait() dậy
    }
}

public class waitandnotify {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount();
        new Thread(() -> acc.withdraw(100)).start(); // Luồng 1: Đòi rút tiền ngay (sẽ bị ngủ chờ)
        new Thread(() -> { // Luồng 2: 2 giây sau mới nạp tiền vào
            try { Thread.sleep(2000); } catch (Exception e) {}
            acc.deposit(100);
        }).start();
    }
}