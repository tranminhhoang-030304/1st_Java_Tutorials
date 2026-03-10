package javacorebasic.chapter2_oop.a_classandobject;

public class chapter2a_hàmMainOOP {
    public static void main(String[] args) {

        // 2. OBJECT: Tạo ra các thực thể (đối tượng) từ bản thiết kế Student

        // Tạo đối tượng sinh viên 1
        chapter2a_ClassStudent st1 = new chapter2a_ClassStudent(); // Từ khóa 'new' dùng để khởi tạo đối tượng
        st1.name = "Hoang";
        st1.age = 22;

        // Tạo đối tượng sinh viên 2
        chapter2a_ClassStudent st2 = new chapter2a_ClassStudent();
        st2.name = "Alice";
        st2.age = 20;

        // Gọi hành động của đối tượng
        st1.study(); // In ra: Hoang đang học lập trình Java.
        st2.study(); // In ra: Alice đang học lập trình Java.
    }
}
