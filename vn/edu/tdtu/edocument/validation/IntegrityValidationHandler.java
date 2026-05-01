package vn.edu.tdtu.edocument.validation;

import vn.edu.tdtu.edocument.model.Document;

public class IntegrityValidationHandler extends ValidationHandler {
    @Override
    protected void doCheck(Document doc) {
        System.out.println("[KIỂM DUYỆT - TRẠM 3] Đang kiểm tra tính toàn vẹn (chống trùng lặp)...");

        // Giả lập: Kiểm tra ID trùng lặp (Trong thực tế sẽ query Database)
        if ("DUPLICATE_ID".equals(doc.id)) {
            throw new ValidationException("Hồ sơ này đã được nộp từ trước (Phát hiện trùng lặp).");
        }
    }
}