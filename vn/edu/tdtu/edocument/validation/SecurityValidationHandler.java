package vn.edu.tdtu.edocument.validation;

import vn.edu.tdtu.edocument.model.Document;

public class SecurityValidationHandler extends ValidationHandler {
    @Override
    protected void doCheck(Document doc) {
        System.out.println("[KIỂM DUYỆT - TRẠM 2] Đang quét an toàn bảo mật (Antivirus)...");

        // Giả lập: Nếu tên file chứa từ "virus" thì báo lỗi mã độc
        if (doc.filePath != null && doc.filePath.toLowerCase().contains("virus")) {
            throw new ValidationException("Phát hiện nguy cơ mã độc trong tệp đính kèm!");
        }
    }
}