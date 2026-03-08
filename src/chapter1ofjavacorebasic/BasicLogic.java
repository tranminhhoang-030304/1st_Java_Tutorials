public class BasicLogic {
    public static void main(String[] args){
        //1. Khai báo kiểu dữ liệu
        int age = 22;
        boolean isStudent = true;
        String role = "Admin";

        //2. Câu lệnh if-else
        System.out.println("Check If-Else");
        if(age>=18 && isStudent){
            System.out.println("Sinh viên trưởng thành");
        } else{
            System.out.println("Sinh viên không hợp lệ");
        }

        //3. Câu lệnh switch-case
        System.out.println("Check Switch-Case");
        switch (role){
            case "Admin":
                System.out.println("Quyền quản trị viên");
                break;
            case "User":
                System.out.println("Quyền người dùng");
                break;
            default:
                System.out.println("Không xác định được role!");
        }

        //4. Vòng lặp for (Biết số lần lặp)
        System.out.println("Vòng lặp for");
        for(int i=1;i<=5;i++){
            System.out.println("Lần lặp thứ:" +i);
        }

        //5. Vòng lặp while (Kiểm tra điều kiện trước khi lặp)
        System.out.println("Vòng lặp while");
        int count = 1;
        while (count <= 5){
            System.out.println("Lần lặp thứ:" +count);
            count++;
        }

        //6. Vòng lặp do-while (Thực thi ít nhất 1 lần trước khi kiểm tra điều kiện)
        System.out.println("Vòng lặp do-while");
        int doCount = 8;
        do{
            System.out.println("Luôn chạy lần đầu tiên. Kết quả:" +doCount);
            doCount++;
        } while (doCount < 8);
    }
}
