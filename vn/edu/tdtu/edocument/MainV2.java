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

        // 4. Khởi tạo Document từng bước (Yêu cầu 1: Builder Pattern)
        System.out.println("\n--- BƯỚC 1: ĐIỀN THÔNG TIN CÁ NHÂN VÀ LƯU NHÁP ---");
        String docId = UUID.randomUUID().toString().substring(0, 8);
        Document.Builder builder = new Document.Builder()
            .withId(docId)
            .withApplicantInfo("Nguyen Van A", "nva@gmail.com", "0901234567")
            .withDocumentType("DON_XIN_PHEP");

        Document draft1 = builder.withStatus("NHAP").build();
        System.out.println("-> " + draft1);

        System.out.println("\n--- BƯỚC 2: TẢI TÀI LIỆU ĐÍNH KÈM VÀ LƯU NHÁP ---");
        // Giả lập tạo 1 file dummy_path.pdf để processor có thể đọc được
        try {
            java.io.FileWriter fw = new java.io.FileWriter("dummy_path.pdf");
            fw.write("Day la noi dung file dummy PDF.");
            fw.close();
        } catch (Exception e) {}

        builder.withFileInfo("dummy_path.pdf", "pdf", 1024)
               .withOfficerInfo("Can Bo B", "cbb@tdtu.edu.vn", "0987654321");

        Document draft2 = builder.withStatus("NHAP").build();
        System.out.println("-> " + draft2);

        System.out.println("\n--- BƯỚC 3: XÁC NHẬN VÀ NỘP HỒ SƠ ---");
        builder.withSignatureAndContent("signature_abc123", null);
        
        Document finalDoc = builder.withStatus("MOI_TAO").build();
        System.out.println("-> Hồ sơ đã hoàn tất khởi tạo. Đang gửi đi xử lý...");

        // 5. Chạy luồng xử lý
        processor.process(finalDoc);

        // Chạy thêm một hồ sơ giả định với ảnh PNG để kiểm thử ImageOcrExtractor
        System.out.println("\n=== KIỂM THỬ XỬ LÝ ẢNH PNG ===");
        Document imgDoc = new Document.Builder()
            .withId("IMG9999")
            .withApplicantInfo("Tran Thi C", "ttc@gmail.com", "0999999999")
            .withOfficerInfo("Can Bo B", "cbb@tdtu.edu.vn", "0987654321")
            .withDocumentType("HO_SO_THUE")
            .withFileInfo("invoice.png", "png", 2048)
            .withSignatureAndContent("sig_png", null)
            .withStatus("MOI_TAO")
            .build();
        processor.process(imgDoc);
    }
}
