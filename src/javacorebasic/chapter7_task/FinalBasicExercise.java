package javacorebasic.chapter7_task;

import java.io.InputStream; //thư viện đọc file
import java.sql.*; //thư viện làm việc với DB
import java.util.*; //thư viện dùng Scanner, List, ...

public class FinalBasicExercise {

    // Khởi tạo Properties để đọc file cấu hình
    private static final Properties config = new Properties(); //Tạo 1 đối tượng dùng để lưu trữ data dạng Key-Value

    public static void main(String[] args) {
        // Sử dụng getResourceAsStream để mở ra luồng đọc (InputStream) file config.properties từ trong thư mục src (classpath)
        try (InputStream is = FinalBasicExercise.class.getResourceAsStream("/javacorebasic/chapter7_task/config.properties")) {
            if (is == null) {
                System.out.println("❌ Không tìm thấy file config.properties trong package!");
                return;
            }
            config.load(is); //lấy nội dung từ luồng đọc để nạp vào biến config ở trên
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi đọc file config: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in); //Mở cổng đợi các thao tác từ màn hình Console (System.in)

        // Mở comment từng hàm để chạy test thử nhé:
        taskA_GuessNumber(scanner);
        taskB_CalculateElectricity(scanner);
        taskC_QuickSortFromFile();
        taskD_InsertStudents(scanner);

        scanner.close();
    }

    // HÀM BẮT LỖI NHẬP LIỆU: Lặp vô tận cho đến khi người dùng nhập đúng kiểu dữ liệu đầu vào
    private static int getValidInt(Scanner scanner, String prompt) {
        while (true) { //vòng lặp vô tận chỉ dừng khi gặp 'break' hoặc 'return'
            System.out.print(prompt);
            String input = scanner.nextLine().trim(); // Đọc cả dòng dưới dạng chuỗi; .trim() để loại bỏ khoảng trắng ở 2 đầu
            try {
                return Integer.parseInt(input);  // Thử ép kiểu chuỗi đó sang số nguyên, thành công, return trả về số đó và kết thúc vòng lặp, ngược lại hiển thị lỗi 'number format exception'
            } catch (NumberFormatException e) { //In ra màn hình thông báo lỗi NumberFormatException, vòng lặp while(true) lặp lại từ đầu, yêu cầu user nhập lại
                // Nếu ép kiểu thất bại (nhập chữ), báo lỗi và vòng lặp sẽ chạy lại
                System.out.println("   👉 LỖI: Vui lòng chỉ nhập số nguyên hợp lệ! Hãy thử lại.");
            }
        }
    }

    // CÂU A: ĐOÁN SỐ
    public static void taskA_GuessNumber(Scanner scanner) {
        System.out.println("\n--- CÂU A: ĐOÁN SỐ ---");
        int target = Integer.parseInt(config.getProperty("task.a.target_number")); //lấy cấu hình từ file config.properties mặc định dạng string rồi ép sang int để tính toán
        int maxAttempts = Integer.parseInt(config.getProperty("task.a.max_attempts"));
        int attempts = 0;

        while (attempts < maxAttempts) {
            // Sử dụng hàm getValidInt thay cho scanner.nextInt()
            int guess = getValidInt(scanner, "Nhập số dự đoán (Còn " + (maxAttempts - attempts) + " lần): ");
            attempts++;

            if (guess == target) {
                System.out.println("🎉 Thành công! Bạn đã đoán đúng số " + target);
                return;
            } else {
                System.out.println("❌ Sai rồi!");
            }
        }
        System.out.println("💀 Nhập lỗi! Bạn đã hết " + maxAttempts + " lần thử. Số đúng là: " + target);
    }

    // CÂU B: TÍNH TIỀN ĐIỆN
    public static void taskB_CalculateElectricity(Scanner scanner) {
        System.out.println("\n--- CÂU B: TÍNH TIỀN ĐIỆN ---");
        int t1Limit = Integer.parseInt(config.getProperty("task.b.tier1_limit"));
        int t1Price = Integer.parseInt(config.getProperty("task.b.tier1_price"));
        int t2Limit = Integer.parseInt(config.getProperty("task.b.tier2_limit"));
        int t2Price = Integer.parseInt(config.getProperty("task.b.tier2_price"));
        int t3Price = Integer.parseInt(config.getProperty("task.b.tier3_price"));

        // Sử dụng hàm getValidInt
        int kwh = getValidInt(scanner, "Nhập số điện tiêu thụ trong tháng: ");
        long totalCost = 0;

        if (kwh <= t1Limit) {
            totalCost = (long) kwh * t1Price;
        } else if (kwh <= t2Limit) {
            totalCost = (long) t1Limit * t1Price + (long) (kwh - t1Limit) * t2Price;
        } else {
            totalCost = (long) t1Limit * t1Price +
                    (long) (t2Limit - t1Limit) * t2Price +
                    (long) (kwh - t2Limit) * t3Price;
        }

        System.out.println("💰 Tổng tiền điện phải trả: " + totalCost + " VNĐ");
    }

    // CÂU C: QUICK SORT TỪ FILE
    public static void taskC_QuickSortFromFile() {
        System.out.println("\n--- CÂU C: QUICK SORT TỪ FILE ---");
        List<Integer> list = new ArrayList<>();

        try (InputStream is = FinalBasicExercise.class.getResourceAsStream("/javacorebasic/chapter7_task/input.txt")) {
            if (is == null) {
                System.out.println("❌ Không tìm thấy file input.txt trong package!");
                return;
            }
            Scanner fileScanner = new Scanner(is); //tạo 1 luồng đọc file (is) để hệ thống đọc file input.txt
            while (fileScanner.hasNextInt()) { //check số tiếp theo trong file có phải là int hay không, có thì vào vòng lặp lấy ra cho vào ArrayList
                list.add(fileScanner.nextInt());
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("❌ Lỗi đọc file input.txt: " + e.getMessage());
            return;
        }

        int[] arr = list.stream().mapToInt(i -> i).toArray(); //cấu trúc biến đổi 1 dạng danh sách (list<integer> sang mảng nguyên thủy (int[])
        System.out.println("Mảng ban đầu: " + Arrays.toString(arr));

        quickSort(arr, 0, arr.length - 1);

        System.out.println("Mảng sau khi Quick Sort: " + Arrays.toString(arr));
    }
    //hàm partition và quicksort chia mảng ra làm 2 nửa dựa vào 1 điểm chốt, các số nhỏ hơn chốt sang trái, lớn hơn sang phải đến khi sắp xếp xong
    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    // CÂU D: NHẬP VÀ INSERT SINH VIÊN VÀO DB
    public static void taskD_InsertStudents(Scanner scanner) {
        System.out.println("\n--- CÂU D: NHẬP VÀ LƯU SINH VIÊN VÀO DB ---");

        String url = config.getProperty("db.url");
        String user = config.getProperty("db.username");
        String pass = config.getProperty("db.password");
        //Khai báo câu lệnh SQL với các dấu ?. Dấu ? là các "lỗ hổng" chờ mình bơm dữ liệu vào sau, giúp chống lại nạn hacker chèn mã độc (SQL Injection).
        String sql = "INSERT INTO students (name, gender, hometown, age) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, pass); //Kết nối đường dẫn DB, tài khoản, mật khẩu, nó sẽ mở cánh cửa kết nối giữa Java và MySQL.
             PreparedStatement pstmt = conn.prepareStatement(sql)) { //Đưa câu lệnh SQL có chứa dấu ? ở trên cho DB phân tích và biên dịch trước.

            // Sử dụng hàm getValidInt và KHÔNG cần scanner.nextLine() để trôi lệnh nữa
            int n = getValidInt(scanner, "Bạn muốn nhập bao nhiêu sinh viên (n)? ");

            for (int i = 1; i <= n; i++) {
                System.out.println("\n--- Nhập sinh viên thứ " + i + " ---");
                System.out.print("Tên (Không trùng lặp): ");
                String name = scanner.nextLine();

                System.out.print("Giới tính: ");
                String gender = scanner.nextLine();

                System.out.print("Quê quán: ");
                String hometown = scanner.nextLine();

                // Sử dụng hàm getValidInt
                int age = getValidInt(scanner, "Tuổi: ");

                pstmt.setString(1, name); //Bơm cái giá trị name (tên sinh viên) vào cái dấu ? thứ nhất. (Tương tự cho các giá trị sau).
                pstmt.setString(2, gender);
                pstmt.setString(3, hometown);
                pstmt.setInt(4, age);
                pstmt.addBatch(); //gói dữ liệu thông tin sinh viên vừa nhập tạm vào RAM Java chờ Enter mới insert xuống DB
            }

            System.out.print("\nBấm [ENTER] để lưu toàn bộ vào DB...");
            scanner.nextLine(); //đọc một dòng ký tự bạn gõ từ bàn phím cho đến khi gõ Enter, k gán nó vào biến nào cả (String temp = scanner.nextLine() -> bỏ qua phần String temp =) Lợi dụng tính chất "chờ người dùng thao tác" của lệnh này để làm chương trình tạm dừng lại (Pause).

            int[] results = pstmt.executeBatch(); //kích hoạt JDBC lấy toàn bộ thông tin các sinh viên trong RAM, mở một kết nối mạng (thông qua cổng 3306 của MySQL), và gửi một gói tin duy nhất chứa tất cả các lệnh INSERT xuống Database. MySQL nhận được gói tin, bung ra, lưu vào ổ cứng, và trả về một mảng int[] results (chứa số lượng dòng đã thêm thành công)
            System.out.println("🎉 THÀNH CÔNG: Đã thêm " + results.length + " sinh viên vào DB!");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ LỖI: Có sinh viên bị TRÙNG TÊN. Vui lòng kiểm tra lại!");
        } catch (SQLException e) {
            System.out.println("❌ LỖI DATABASE: " + e.getMessage());
        }
    }
}