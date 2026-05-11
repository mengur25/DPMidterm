package vn.edu.tdtu.edocument.validation;

import vn.edu.tdtu.edocument.model.Document;

public abstract class ValidationHandler {
    protected ValidationHandler nextHandler;

    // Thiết lập trạm tiếp theo
    public ValidationHandler setNext(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    // Luồng thực thi
    public void check(Document doc) {
        // 1. Trạm hiện tại thực hiện kiểm tra
        doCheck(doc);

        // 2. Nếu không có lỗi, chuyển hồ sơ sang trạm kế tiếp
        if (nextHandler != null) {
            nextHandler.check(doc);
        }
    }

    // Lớp con sẽ ghi đè phương thức này để cài đặt logic kiểm duyệt riêng
    protected abstract void doCheck(Document doc);
}