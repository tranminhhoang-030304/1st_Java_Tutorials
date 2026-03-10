package javacorebasic.chapter2_oop.c_d_khaibaovaphanchiacacclass_vd;

//Khai báo Class, thuộc tính và phương thức của Class
public class Oto2 {
    private String brand;
    private String color;
    private int year;
    private double price;
    private int hp;
    public static int soLuong = 0;


    public void raccing(){
        System.out.println("Hãng: " + brand + "|| Màu: " + color + "|| Năm sản xuất: " + year + "|| Giá: " + price + "|| Mã lực: " + hp + "đang đua trên đường đua F1");
    }

    public String getBrand(){
        return this.brand;
    }

    public Oto2(){
        this.brand = "Ferari";
        this.color = "Red";
        this.year = 2020;
        this.price = 36000;
        this.hp = 360;
        soLuong++;
    }

    public Oto2(String brand, String color, int year, int price, int hp){
        this.brand = brand;
        this.color = color;
        this.year = year;
        this.price = price;
        this.hp = hp;
        soLuong++;
    }

    public class MainCar{
        public static void main(String[] args){
            Oto car1 = new Oto();
            car1.raccing();

            Oto car2 = new Oto("Audi", "Đen", 2022, 360000, 360);
            car2.raccing();

            Oto car3 = new Oto("Toyota", "White", 2024, 36000, 36);
            car3.raccing();

            Oto car4 = new Oto("Aston Martin", "Navy Blue", 2023, 99000, 120);
            car4.raccing();
        }
    }

    public static void main(String[] args) {
        System.out.println("Danh sách xe đua F1");

        // 1. Vẫn tạo các đối tượng xe từ bản thiết kế Oto như bình thường
        Oto car1 = new Oto();
        Oto car2 = new Oto("Audi", "Đen", 2022, 360000, 360);
        Oto car3 = new Oto("Toyota", "White", 2024, 36000, 36);
        Oto car4 = new Oto("Aston Martin", "Navy Blue", 2023, 99000, 120);

        // 2. Gom tất cả các xe vừa tạo vào một Mảng (Array) có kiểu dữ liệu là 'Oto'
        Oto[] danhSach = {car1, car2, car3, car4};

        // 3. Dùng vòng lặp for-each để ra lệnh cho TẤT CẢ các xe trong mảng cùng chạy
        // Đọc là: "Với mỗi chiếc 'xe' kiểu Oto nằm trong 'danhSach'..."
        for (Oto xe : danhSach) {
            xe.raccing();
        }

        // 4. Lấy tổng số lượng xe tự động bằng thuộc tính 'length' (độ dài) của mảng
        System.out.println("Danh sách gồm " + danhSach.length + " xe");
    }
}
//dùng mảng