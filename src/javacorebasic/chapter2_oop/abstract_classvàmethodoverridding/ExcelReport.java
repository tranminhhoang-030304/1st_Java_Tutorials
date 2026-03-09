package javacorebasic.chapter2_oop.abstract_classvàmethodoverridding;

public class ExcelReport extends BaseReport {
    @Override //Bắt buộc triển khai hàm abstract của class cha
    public void exportExcel(){
        System.out.println("Xuất dữ liệu ra file Excel...");
    }
    @Override //Method Overridding: Ghi đè là hàm connectDatabase() đã có sẵn của class cha
    public void connectDatabase(){
        System.out.println("Kết nối với DB cho báo cáo Excel bằng acc Admin ...");
    }
}

//Method Overridding: Xảy ra khi một Class con kế thừa Class cha, nhưng Class con "không phục" hoặc muốn "làm khác đi" cách thức hoạt động của một phương thức đã có sẵn ở Class cha. Lúc này, Class con sẽ viết lại toàn bộ nội dung của phương thức đó
//Features: Phải có tính thừa kế (extends hoặc implements); Tên phương thức và Danh sách tham số đầu vào giống hệt nhau (tham số khác sẽ thành Overloading); Trả về giống hệt hoặc là class con của kiểu ban đầu
