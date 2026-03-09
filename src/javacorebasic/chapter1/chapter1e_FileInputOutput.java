package javacorebasic.chapter1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class chapter1e_FileInputOutput {
    public static void main(String[] args){
        String fileName = "javacorebasic/chapter4_DBMySQL/data.txt";

        //1. Ghi file (Output Stream)
        try (FileWriter writer = new FileWriter(fileName)){ // Cú pháp try-with-resources (có dấu ngoặc tròn sau try) giúp tự động đóng file sau khi dùng xong
            writer.write("Streams Input & Output, Files\n");
            writer.write("Ghi file thành công!");
            System.out.println("Dữ liệu đã được ghi vào file" + fileName);
        } catch (IOException e){
            System.out.println("Xảy ra lỗi khi ghi file!" + e.getMessage());
        }
        System.out.println(".........");

        //2. Đọc file (Input Stream)
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            System.out.println("Nội dung đọc từ file:");
            while ((line = reader.readLine()) != null){ // Đọc từng dòng cho đến khi không còn dòng nào (null)
                System.out.println(line);
            }
        } catch (IOException e){
            System.out.println("Xảy ra lỗi khi đọc file!" + e.getMessage());
        }
    }
}
