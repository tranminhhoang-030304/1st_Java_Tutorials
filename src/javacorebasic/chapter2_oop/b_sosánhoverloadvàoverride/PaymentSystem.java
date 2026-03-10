package javacorebasic.chapter2_oop.b_sosánhoverloadvàoverride;

public class PaymentSystem {
    public void processPayment(double amount){
        System.out.println("Pay by Cash: " + amount); //Overloading: Cùng tên "processPayment" nhưng khác tham số đầu vào
    }
    public void processPayment(String bankAccount, double amount){
        System.out.println("Chuyển khoản từ: " + bankAccount + "Số tiền là: " + amount);
    }
}

class ParentReport{
    public void generateReport(){
        System.out.println("Xuất báo cáo chung: ");
    }
}

class RevenueReport extends ParentReport {
    @Override
    public void generateReport(){ //Overridding: Viết lại hoàn toàn nội dung của hàm generateReport từ class cha
        System.out.println("Xuất báo cáo doanh thu chi tiết đã được ghi đè: ");
    }
}

//chapter2b: Tính đa hình
//So sánh Overloading (Nạp chồng) và Overridding (Ghi đè)
//Đặc điểm: Xảy ra trong cùng 1 class || Xảy ra giữa 2 class có thừa kế (cha/con)
//Tên phương thức: Đều bắt buộc phải giống nhau
//Tham số truyền vào: Bắt buộc phải khác nhau (số lượng hoặc kiểu dữ liệu) || Bắt buộc phải y hệt nhau
//Ý nghĩa: Đa hình khi biên dịch (Cùng hành động nhưng xử lý các loại đầu vào khác nhau) || Đa hình khi chạy (Class con muốn tùy biến lại cách làm của class cha)