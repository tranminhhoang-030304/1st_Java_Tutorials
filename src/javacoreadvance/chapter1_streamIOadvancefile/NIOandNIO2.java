package javacoreadvance.chapter1_streamIOadvancefile;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class NIOandNIO2 {
    public static void main(String[] args) throws Exception {
        Path path = Path.of("nio_test.txt"); //tạo 1 file nháp chứa dữ liệu
        Files.writeString(path, "DataAnalytics_Event1\nDataAnalytics_Event2\n");
        System.out.println("Cách 1: Dùng Channel và Buffer (NIO basic)"); //RandomAccessFile cho phéo đọc/ghi bất kì vị trí nào của file
        try (RandomAccessFile file = new RandomAccessFile("nio_test.txt","rw");
            FileChannel channel = file.getChannel()){
            ByteBuffer buffer = ByteBuffer.allocate(48); //tạo khối đệm buffer size 48 bytes trong RAM
            int byteRead = channel.read(buffer); //Channel hút data từ file đổ vào buffer
            while (byteRead != -1){
                buffer.flip(); //lật (flip) buffer từ "đang ghi" sang chuẩn bị đọc ra
                while (buffer.hasRemaining()){ // đọc từng byte trong buffer ra màn hình
                    System.out.print((char) buffer.get());
                }
                buffer.clear(); //dọn dẹp buffer để channel đổ mẻ data mới
                byteRead = channel.read(buffer);
            }
        }
        System.out.println("\n");
        
        System.out.println("Cách 2: NIO2 (Package java.nio.files");
        System.out.println("File size: " + Files.size(path) + " bytes"); // lấy thông tin metadata
        System.out.println("File is readable: " + Files.isReadable(path));
        List<String> lines = Files.readAllLines(path); //đọc toàn bộ các dòng vào 1 list string chỉ với 1 lệnh
        System.out.println("Dòng thứ 2 trong fie là: " + lines.get(1));
    }
}
