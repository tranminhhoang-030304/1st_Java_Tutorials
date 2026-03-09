package javacorebasic.chapter2_oop.inheritance_kếthừa;

// CLASS CON kế thừa CLASS CHA
public class DigitalProduct extends Product {
    private String downloadLink;

    // DigitalProduct tự động có name, price và displayInfo() từ Product
    public void setDetails(String name, double price, String link) {
        this.name = name; // Thuộc tính của class cha
        this.price = price; // Thuộc tính của class cha
        this.downloadLink = link;
    }
}
//Thừa kế cho phép một class mới (Class con) kế thừa lại toàn bộ thuộc tính và phương thức của một class đã có sẵn (Class cha)
//Trong Java, chúng ta dùng từ khóa "extends". Java chỉ hỗ trợ đơn kế thừa (một con chỉ có một cha).