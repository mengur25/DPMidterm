package vn.edu.tdtu.edocument;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.notification.*;
import vn.edu.tdtu.edocument.storage.*;
import vn.edu.tdtu.edocument.service.DocumentProcessor;

import java.util.UUID;

public class MainV2 {
    public static void main(String[] args) {
        System.out.println("=== CHƯƠNG TRÌNH MÔ PHỎNG V2.0 ===");

        // 1. Khởi tạo NotificationManager (Yêu cầu 4: Observer Pattern)
        NotificationManager notificationManager = new NotificationManager();
        notificationManager.subscribe(new EmailNotifier());
        notificationManager.subscribe(new SMSNotifier());
        notificationManager.subscribe(new AppPushNotifier()); // Có thể thêm/bớt kênh dễ dàng

        // 2. Lựa chọn chiến lược lưu trữ (Yêu cầu 5: Strategy Pattern)
        // Chúng ta có thể dễ dàng chuyển đổi sang DatabaseStorage hoặc CloudStorage
        DocumentStorage storage = new LocalJsonStorage();
        // DocumentStorage storage = new DatabaseStorage();
        // DocumentStorage storage = new CloudStorage();

        // 3. Khởi tạo DocumentProcessor
        DocumentProcessor processor = new DocumentProcessor(notificationManager, storage);

        // 4. Tạo Document giả định để xử lý
        Document doc1 = new Document(
            UUID.randomUUID().toString().substring(0, 8),
            "Nguyen Van A", "nva@gmail.com", "0901234567",
            "Can Bo B", "cbb@tdtu.edu.vn", "0987654321",
            "DON_XIN_PHEP",
            "dummy_path.txt", "txt", 1024,
            "signature_abc123",
            null, "MOI_TAO"
        );

        // Giả lập tạo 1 file dummy_path.txt để processor có thể đọc được
        try {
            java.io.FileWriter fw = new java.io.FileWriter("dummy_path.txt");
            fw.write("Day la noi dung file dummy.");
            fw.close();
        } catch (Exception e) {}

        // 5. Chạy luồng xử lý
        processor.process(doc1);
    }
}
