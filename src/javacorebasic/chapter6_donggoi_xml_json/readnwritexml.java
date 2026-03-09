package javacorebasic.chapter6_donggoi_xml_json;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class readnwritexml {
    public static void main(String[] args) {
        try {
            File xmlFile = new File("config.xml");

            // Khởi tạo bộ máy đọc XML của Java
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            // Bắt đầu bóc tách dữ liệu
            NodeList dbNodes = doc.getElementsByTagName("database");
            NodeList portNodes = doc.getElementsByTagName("port");
            NodeList userNodes = doc.getElementsByTagName("user");
            NodeList hostNodes = doc.getElementsByTagName("host");
            NodeList passwordNodes = doc.getElementsByTagName("password");

            System.out.println("--- ĐỌC DỮ LIỆU TỪ XML ---");
            System.out.println("Loại Database: " + dbNodes.item(0).getTextContent());
            System.out.println("User Database: " + userNodes.item(0).getTextContent());
            System.out.println("Host Database: " + hostNodes.item(0).getTextContent());
            System.out.println("Password Database: " + passwordNodes.item(0).getTextContent());
            System.out.println("Cổng kết nối Database: " + portNodes.item(0).getTextContent());

        } catch (Exception e) {
            System.out.println("Lỗi đọc XML: Chưa có file config.xml. Bạn hãy tạo file này để chạy thử nhé!");
        }
    }
}
