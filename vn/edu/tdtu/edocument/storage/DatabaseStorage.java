package vn.edu.tdtu.edocument.storage;

import vn.edu.tdtu.edocument.model.Document;

public class DatabaseStorage implements DocumentStorage {
    @Override
    public void save(Document document) {
        System.out.println("  [LƯU TRỮ] -> Đang kết nối CSDL MySQL...");
        System.out.println("  [LƯU TRỮ] -> Đã lưu hồ sơ [" + document.id + "] vào CSDL thành công.");
    }
}
