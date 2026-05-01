package vn.edu.tdtu.edocument.validation;

import java.io.File;
import java.util.Arrays; // Thêm import này
import java.util.List; // Thêm import này

import vn.edu.tdtu.edocument.model.Document;

public class BasicValidationHandler extends ValidationHandler {
    @Override
    protected void doCheck(Document doc) {
        System.out.println("[KIỂM DUYỆT - TRẠM 1] Đang kiểm tra tính hợp lệ cơ bản...");

        // 1. Kiểm tra thông tin liên lạc: Bắt buộc chuẩn Email bằng Regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        if (doc.applicantEmail == null || !doc.applicantEmail.matches(emailRegex)) {
            throw new ValidationException("Email người nộp không đúng định dạng.");
        }
        if (doc.officerEmail == null || !doc.officerEmail.matches(emailRegex)) {
            throw new ValidationException("Email cán bộ tiếp nhận không đúng định dạng.");
        }

        // 2. Kiểm tra tệp đính kèm: Xác nhận thực sự tồn tại trên ổ cứng
        if (doc.filePath == null || doc.filePath.isEmpty()) {
            throw new ValidationException("Chưa đính kèm đường dẫn tệp tài liệu.");
        }
        File attachedFile = new File(doc.filePath);
        if (!attachedFile.exists() || !attachedFile.isFile()) {
            throw new ValidationException("Tệp đính kèm không thực sự tồn tại trên hệ thống (" + doc.filePath + ").");
        }

        // 3. Kiểm tra dung lượng: Không vượt quá 5MB
        if (doc.fileSizeKB > 5120) {
            throw new ValidationException("Dung lượng file " + doc.fileSizeKB + "KB vượt quá 5MB.");
        }

        // 4. Kiểm tra định dạng: Chỉ chấp nhận txt, pdf, png, jpg
        String ext = doc.fileExtension != null ? doc.fileExtension.toLowerCase() : "";
        List<String> allowedExtensions = Arrays.asList("txt", "pdf", "png", "jpg");

        if (!allowedExtensions.contains(ext)) {
            throw new ValidationException("Định dạng file ." + ext + " không hợp lệ. Chỉ hỗ trợ: " + allowedExtensions);
        }
    }
}