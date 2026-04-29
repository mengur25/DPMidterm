package vn.edu.tdtu.edocument.extractor;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TextExtractor implements DocumentExtractor {
    @Override
    public String extractContent(String filePath) throws Exception {
        System.out.println("   -> [TextExtractor] Đang đọc nội dung text từ file thuần túy...");
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
