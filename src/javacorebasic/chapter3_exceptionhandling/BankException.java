package javacorebasic.chapter3_exceptionhandling;

public class BankException {
    public static int rutTien(int soDu, int soTienRut) throws Exception{
        if (soTienRut > soDu){
            throw new Exception("Lỗi! Số dư không đủ!");
        }
        if (soTienRut <= 0){
            throw new Exception("Lỗi! Số tiền rút phải > 0!");
        }
        soDu = soDu - soTienRut; // Nếu không có lỗi, tiến hành trừ tiền
        System.out.println("Rút tiền thành công! Máy đang xuất: " + soTienRut + " VND");
        return soDu; // Trả về số dư đã được cập nhật
    }

    public static void main(String[] args){
        System.out.println("Máy đã sẵn sàng!");
        int soDuUser = 36000000;
        int soTienCanRut = 204528;
        try {
            System.out.println("User muốn rút " + soTienCanRut + " VND!");
            soDuUser = rutTien(soDuUser, soTienCanRut);
            System.out.println("Giao dịch thành công!");
        } catch (Exception e) {
            System.err.println("Giao dịch không thành công! " + e.getMessage());
        } finally {
            System.out.println("Số dư hiện tại: " + soDuUser + " VND"); // 3. IN SỐ DƯ TẠI ĐÂY
            System.out.println("Cảm ơn quý khách đã sử dụng dịch vụ!\n"); // Dù thành công hay văng lỗi đỏ chót, dòng in số dư này CHẮC CHẮN vẫn hiện ra!
        }
        System.out.println("Máy vẫn bình thường, chờ giao dịch tiếp theo!");
    }
}

//Ngoại lệ là những sự cố hoặc lỗi phát sinh trong lúc chương trình đang chạy (Runtime). Nếu không được dự liệu và xử lý, ngoại lệ sẽ làm đứt gãy luồng hoạt động, khiến chương trình bị crash ngay lập tức.

//3.a. Khai báo ngoại lệ
//Là hành động "dán nhãn cảnh báo" cho một phương thức (hàm). Nó thông báo cho hệ thống (và các lập trình viên khác) biết rằng khi gọi hàm này có nguy cơ sẽ xảy ra lỗi
//Sử dụng từ khóa throws đặt ở cuối phần định nghĩa tên hàm.

//3.b. Cơ chế try-catch
//Định nghĩa: Là "tấm khiên" bảo vệ giúp chương trình sống sót qua các lỗi và tiếp tục chạy bình thường.
//Cấu trúc:
//Khối try: Nơi bạn đặt những dòng code "nguy hiểm", có nguy cơ phát sinh lỗi.
//Khối catch: Nơi "tóm" lấy lỗi. Nếu code trong khối try nổ bom (xảy ra lỗi), chương trình không bị chết mà sẽ lập tức nhảy vào khối catch để bạn xử lý (ví dụ: in ra thông báo lỗi màu đỏ).
//Khối finally (đi kèm): Là khối lệnh "vô cực", tức là chắc chắn sẽ được chạy vào phút cuối, bất chấp trước đó có xảy ra lỗi hay không. Thường dùng để dọn dẹp hệ thống (như đóng file, in số dư cuối cùng).

//3.c. Throw ngoại lệ
//Là chủ động tạo ra một lỗi và "ném" nó ra ngoài.
//Thường dùng khi chương trình không bị lỗi cú pháp hay hệ thống, nhưng lại vi phạm logic nghiệp vụ (Ví dụ: Tuổi nhập vào là số âm, rút tiền vượt quá số dư). Lúc này, dùng từ khóa 'throw new' để ép chương trình phải ghi nhận đó là một lỗi.

//3.d. Chuyển tiếp ngoại lệ
//Là cơ chế chuyển lỗi đi
//Cách hoạt động: Khi một hàm có đoạn code sinh ra lỗi, nhưng hàm đó quyết định không tự xử lý (không dùng try-catch). Thay vào đó, dùng lệnh throws để đẩy cái lỗi đó lên cho phương thức cấp cao hơn (phương thức đã gọi nó) giải quyết.