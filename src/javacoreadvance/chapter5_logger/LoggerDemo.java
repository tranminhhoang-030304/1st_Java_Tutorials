package javacoreadvance.chapter5_logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerDemo {
    private static final Logger logger  = Logger.getLogger(LoggerDemo.class.getName()); // Khởi tạo Logger cho class này
    public static void main(String[] args){
        cauHinhLuuVaoFile();
        System.out.println("Bắt đầu chương trình");
        logger.info("Hệ thống khởi động thành công lúc 14h36"); // 1. Ghi log thông tin bình thường
        logger.warning("Ổ cứng sắp đầy, chỉ còn 500MB!"); // 2. Ghi log cảnh báo
        try { // 3. Ghi log lỗi nghiêm trọng (Thường dùng trong try-catch)
            int results = 10 / 0;
        } catch (Exception e) { // Lệnh này ghi kèm luôn cả cái lỗi (Exception) vào log để sau này trace lại
            logger.log(Level.SEVERE, "Lỗi toán học xảy ra!", e);
        }
        System.out.println("Kết thúc chương trình");
    }
    private static void cauHinhLuuVaoFile(){ // Hàm thiết lập để Logger ghi ra file thay vì chỉ in ra màn hình
        try { // Tạo file log tên là "app_log.txt", tham số 'true' là để ghi tiếp (append) chứ không ghi đè mất file cũ
            FileHandler fileHandler = new FileHandler("app_log.txt", true);
            fileHandler.setFormatter(new SimpleFormatter()); // Định dạng log ra text thường cho dễ đọc (mặc định của JUL là định dạng XML hơi rối)
            logger.addHandler(fileHandler); // Gắn cái vòi ghi file vào Logger
        } catch (IOException e){
            logger.log(Level.SEVERE, "Không thể tạo file log", e);
        }
    }
}
