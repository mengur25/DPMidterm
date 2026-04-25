package vn.edu.tdtu.edocument.storage;

import vn.edu.tdtu.edocument.model.Document;

public class CloudStorage implements DocumentStorage {
    @Override
    public void save(Document document) {
        System.out.println("  [LƯU TRỮ] -> Đang upload tệp lên AWS S3...");
        System.out.println("  [LƯU TRỮ] -> Đã lưu dữ liệu hồ sơ [" + document.id + "] trên Cloud thành công.");
    }
}
