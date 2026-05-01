package vn.edu.tdtu.edocument.service;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.notification.NotificationManager;
import vn.edu.tdtu.edocument.storage.DocumentStorage;
import vn.edu.tdtu.edocument.validation.ValidationException;
import vn.edu.tdtu.edocument.validation.ValidationHandler;

public class DocumentProcessor {

    private NotificationManager notificationManager;
    private DocumentStorage documentStorage;
    private ValidationHandler validationChain;

    public DocumentProcessor(NotificationManager notificationManager, DocumentStorage documentStorage,
            ValidationHandler validationChain) {
        this.notificationManager = notificationManager;
        this.documentStorage = documentStorage;
        this.validationChain = validationChain;
    }

    public void process(Document doc) {
        System.out.println("\n=======================================================");
        System.out.println("BẮT ĐẦU XỬ LÝ HỒ SƠ ID: " + doc.id);

        // DATA VALIDATION - Kiểm tra các trường thông tin bắt buộc
        try {
            validateRequiredInput(doc);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }

        doc.status = "DA_TIEP_NHAN";
        notificationManager.notifyAll(doc);

        // YÊU CẦU 3: Chain of Responsibility cho kiểm duyệt hồ sơ
        System.out.println("[KIỂM DUYỆT] Hồ sơ bắt đầu đi qua các trạm kiểm tra...");
        try {
            validationChain.check(doc);
        } catch (ValidationException e) {

            System.out.println("[TỪ CHỐI] " + e.getMessage());
            doc.status = "TU_CHOI";
            notificationManager.notifyAll(doc);
            return;
        }

        System.out.println("[TRÍCH XUẤT] Đang chọn công cụ đọc phù hợp với định dạng " + doc.fileExtension + "...");
        vn.edu.tdtu.edocument.extractor.DocumentExtractor extractor = vn.edu.tdtu.edocument.extractor.DocumentExtractorFactory
                .getExtractor(doc.fileExtension);

        if (extractor == null) {
            System.out.println("[TỪ CHỐI] Định dạng " + doc.fileExtension + " chưa được hỗ trợ trích xuất.");
            doc.status = "TU_CHOI";
            notificationManager.notifyAll(doc);
            return;
        }

        try {
            String content = extractor.extractContent(doc.filePath);
            doc.extractedContent = content;
        } catch (Exception e) {
            System.out.println("[LỖI] Trích xuất nội dung thất bại: " + e.getMessage());
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

    private void validateRequiredInput(Document doc) throws RuntimeException {
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

            throw new RuntimeException("[LỖI TIẾP NHẬN] Thiếu trường thông tin bắt buộc. Hủy tạo hồ sơ.");
        }
    }
}