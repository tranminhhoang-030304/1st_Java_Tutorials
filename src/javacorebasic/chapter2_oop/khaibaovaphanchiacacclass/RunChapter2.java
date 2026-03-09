package javacorebasic.chapter2_oop.khaibaovaphanchiacacclass;

// 1. (Class)
class SmartPhone {
    private String name;
    private int price;

    // Constructor (Hàm khởi tạo)
    public SmartPhone(String name, int price) {
        this.name = name;
        this.price = price;
    }

    // Phương thức (Hành động)
    public void printInfo() {
        System.out.println("📱 Điện thoại: " + name + " | Giá: " + price + "$");
    }
}

// 2. Hàm main
public class RunChapter2 {

    public static void main(String[] args) {
        System.out.println("--- BẮT ĐẦU KHỞI ĐỘNG HỆ THỐNG ---");

        // Dùng từ khóa 'new' để lấy bản thiết kế đúc ra các đối tượng thật
        SmartPhone phone1 = new SmartPhone("iPhone 15", 999);
        SmartPhone phone2 = new SmartPhone("Samsung Galaxy S24", 899);

        // Ra lệnh cho các đối tượng thực hiện hành động
        phone1.printInfo();
        phone2.printInfo();

        System.out.println("--- KẾT THÚC CHƯƠNG TRÌNH ---");
    }
}
