package vn.edu.tdtu.edocument.extractor;

public class ImageOcrExtractor implements DocumentExtractor {
    @Override
    public String extractContent(String filePath) throws Exception {
        System.out.println("   -> [ImageOcrExtractor] Đang phân tích ảnh (JPG/PNG) bằng AI Vision OCR...");
        // Giả lập phân tích hình ảnh mất thời gian
        Thread.sleep(1500); 
        return "[Nội dung chữ nhận diện được từ hình ảnh] " + filePath;
    }
}
