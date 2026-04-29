package vn.edu.tdtu.edocument.extractor;

public class PdfOcrExtractor implements DocumentExtractor {
    @Override
    public String extractContent(String filePath) throws Exception {
        System.out.println("   -> [PdfOcrExtractor] Đang gọi API dịch vụ OCR để trích xuất text từ file PDF...");
        // Giả lập xử lý OCR mất thời gian
        Thread.sleep(1000); 
        return "[Nội dung trích xuất từ PDF qua OCR] " + filePath;
    }
}
