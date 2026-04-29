package vn.edu.tdtu.edocument.extractor;

public class DocumentExtractorFactory {
    public static DocumentExtractor getExtractor(String extension) {
        if (extension == null) return null;
        
        switch (extension.toLowerCase()) {
            case "txt":
                return new TextExtractor();
            case "pdf":
                return new PdfOcrExtractor();
            case "jpg":
            case "jpeg":
            case "png":
                return new ImageOcrExtractor();
            default:
                return null;
        }
    }
}
