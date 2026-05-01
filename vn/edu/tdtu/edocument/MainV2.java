package vn.edu.tdtu.edocument;

import java.util.UUID;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.notification.AppPushNotifier;
import vn.edu.tdtu.edocument.notification.EmailNotifier;
import vn.edu.tdtu.edocument.notification.NotificationManager;
import vn.edu.tdtu.edocument.notification.SMSNotifier;
import vn.edu.tdtu.edocument.service.DocumentProcessor;
import vn.edu.tdtu.edocument.storage.DocumentStorage;
import vn.edu.tdtu.edocument.storage.LocalJsonStorage;
import vn.edu.tdtu.edocument.validation.ValidationChainFactory;
import vn.edu.tdtu.edocument.validation.ValidationHandler;

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

        ValidationHandler validationChain = ValidationChainFactory.createStandardChain();

        // 3. Khởi tạo DocumentProcessor
        DocumentProcessor processor = new DocumentProcessor(notificationManager, storage, validationChain);

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
        } catch (Exception e) {
        }

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

        // CHẠY YÊU CẦU 3: DEMO TOÀN BỘ CHUỖI KIỂM DUYỆT (CHAIN OF RESPONSIBILITY)

        // 1. Hồ sơ vi phạm Trạm 1 (Basic): Sai định dạng file (.exe)
        System.out.println("\n--- NỘP HỒ SƠ 1 (Lỗi định dạng) ---");
        Document docFailStation1 = new Document.Builder()
                .withId("ERR_001")
                .withApplicantInfo("Tran Van A", "a@gmail.com", "090")
                .withOfficerInfo("Can Bo B", "cbb@tdtu.edu.vn", "098")
                .withDocumentType("DON_XIN_PHEP")
                .withFileInfo("phan_mem.exe", "exe", 500) // file .exe
                .withSignatureAndContent("sig_1", null)
                .withStatus("MOI_TAO").build();
        processor.process(docFailStation1);

        // 2. Hồ sơ vi phạm Trạm 2 (Security): Tên file chứa mã độc
        System.out.println("\n--- NỘP HỒ SƠ 2 (Lỗi bảo mật) ---");
        Document docFailStation2 = new Document.Builder()
                .withId("ERR_002")
                .withApplicantInfo("Tran Van B", "b@gmail.com", "091")
                .withOfficerInfo("Can Bo B", "cbb@tdtu.edu.vn", "098")
                .withDocumentType("DON_XIN_PHEP")
                .withFileInfo("tailieu_virus_trojan.pdf", "pdf", 1024) // Tên file có chữ "virus"
                .withSignatureAndContent("sig_2", null)
                .withStatus("MOI_TAO").build();
        processor.process(docFailStation2);

        // 3. Hồ sơ vi phạm Trạm 3 (Integrity): Trùng ID
        System.out.println("\n--- NỘP HỒ SƠ 3 (Lỗi trùng lặp) ---");
        Document docFailStation3 = new Document.Builder()
                .withId("DUPLICATE_ID") // ID đã tồn tại
                .withApplicantInfo("Tran Van C", "c@gmail.com", "092")
                .withOfficerInfo("Can Bo B", "cbb@tdtu.edu.vn", "098")
                .withDocumentType("DON_XIN_PHEP")
                .withFileInfo("don_xin_hoc.pdf", "pdf", 2048)
                .withSignatureAndContent("sig_3", null)
                .withStatus("MOI_TAO").build();
        processor.process(docFailStation3);
    }
}
