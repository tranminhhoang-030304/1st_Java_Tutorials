package javacorebasic.chapter2_oop.abstract_classvàmethodoverridding;

public abstract class BaseReport {
    //Các class con cùng dùng chung code, không cần viết lại
    public void connectDatabase(){
        System.out.println("Kết nối với DB ...");
    }
    //Hàm trống: Ép class con phải tự định nghĩa
    public abstract void exportExcel();
}

//Dùng Abstract Class khi các class có quan hệ họ hàng mật thiết (ví dụ: ExcelReport, PdfReport cùng là họ hàng của BaseReport) và muốn chúng chia sẻ một số đoạn code chung.

