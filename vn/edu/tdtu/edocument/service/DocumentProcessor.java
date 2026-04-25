package vn.edu.tdtu.edocument.service;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.notification.NotificationManager;
import vn.edu.tdtu.edocument.storage.DocumentStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DocumentProcessor {

    private NotificationManager notificationManager;
    private DocumentStorage documentStorage;

    public DocumentProcessor(NotificationManager notificationManager, DocumentStorage documentStorage) {
        this.notificationManager = notificationManager;
        this.documentStorage = documentStorage;
    }

    public void process(Document doc) {
        System.out.println("\n=======================================================");
        System.out.println("BẮT ĐẦU XỬ LÝ HỒ SƠ ID: " + doc.id);

        if (doc.id == null || doc.id.isEmpty() ||
            doc.applicantName == null || doc.applicantName.isEmpty() ||
            doc.applicantEmail == null || doc.applicantEmail.isEmpty() ||
            doc.applicantPhone == null || doc.applicantPhone.isEmpty() ||
            doc.officerName == null || doc.officerName.isEmpty() ||
            doc.officerEmail == null || doc.officerEmail.isEmpty() ||
            doc.officerPhone == null || doc.officerPhone.isEmpty() ||
            doc.documentType == null || doc.documentType.isEmpty() ||
            doc.filePath == null || doc.filePath.isEmpty() ||
            doc.fileExtension == null || doc.fileExtension.isEmpty() ||
            doc.digitalSignature == null || doc.digitalSignature.isEmpty()) {
            
            System.out.println("[LỖI TIẾP NHẬN] Thiếu trường thông tin bắt buộc. Hủy tạo hồ sơ.");
            return;
        }

        doc.status = "DA_TIEP_NHAN";
        notificationManager.notifyAll(doc);

        System.out.println("[KIỂM DUYỆT] Đang kiểm tra dung lượng và định dạng...");
        if (doc.fileSizeKB > 5120) {
            System.out.println("[TỪ CHỐI] Dung lượng file " + doc.fileSizeKB + "KB vượt quá 5MB.");
            doc.status = "TU_CHOI";
            notificationManager.notifyAll(doc);
            return;
        }

        if (!doc.fileExtension.equalsIgnoreCase("txt")) {
            System.out.println("[TỪ CHỐI] Định dạng " + doc.fileExtension + " không được hỗ trợ ở v1.0.");
            doc.status = "TU_CHOI";
            notificationManager.notifyAll(doc);
            return;
        }

        System.out.println("[TRÍCH XUẤT] Đang đọc nội dung tệp đính kèm...");
        try {
            String content = new String(Files.readAllBytes(Paths.get(doc.filePath)));
            doc.extractedContent = content;
        } catch (IOException e) {
            System.out.println("[LỖI] Không thể đọc nội dung file: " + e.getMessage());
            doc.status = "TU_CHOI";
            notificationManager.notifyAll(doc);
            return;
        }

        System.out.println("[LƯU TRỮ] Đang tiến hành lưu trữ dữ liệu...");
        documentStorage.save(doc);

        System.out.println("[HOÀN TẤT] Hồ sơ hợp lệ và đã được lưu trữ thành công.");
        doc.status = "DANG_XET_DUYET";
        notificationManager.notifyAll(doc);
    }
}