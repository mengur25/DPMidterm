package vn.edu.tdtu.edocument.model;

public class Document {
    public String id;
    public String applicantName;
    public String applicantEmail;
    public String applicantPhone;
    public String officerName;
    public String officerEmail;
    public String officerPhone;
    public String documentType;
    public String filePath;
    public String fileExtension;
    public long fileSizeKB;
    public String digitalSignature;
    public String extractedContent;
    public String status;

    public Document() {
    }

    public Document(String id, String applicantName, String applicantEmail, String applicantPhone,
                    String officerName, String officerEmail, String officerPhone,
                    String documentType, String filePath, String fileExtension, 
                    long fileSizeKB, String digitalSignature, String extractedContent, String status) {
        this.id = id;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.officerName = officerName;
        this.officerEmail = officerEmail;
        this.officerPhone = officerPhone;
        this.documentType = documentType;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
        this.fileSizeKB = fileSizeKB;
        this.digitalSignature = digitalSignature;
        this.extractedContent = extractedContent;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Hồ sơ [%s] - Nộp bởi: %s - Trạng thái: %s", id, applicantName, status);
    }

    public static class Builder {
        private Document document;

        public Builder() {
            document = new Document();
        }

        public Builder withId(String id) {
            document.id = id;
            return this;
        }

        public Builder withApplicantInfo(String name, String email, String phone) {
            document.applicantName = name;
            document.applicantEmail = email;
            document.applicantPhone = phone;
            return this;
        }

        public Builder withOfficerInfo(String name, String email, String phone) {
            document.officerName = name;
            document.officerEmail = email;
            document.officerPhone = phone;
            return this;
        }

        public Builder withDocumentType(String documentType) {
            document.documentType = documentType;
            return this;
        }

        public Builder withFileInfo(String filePath, String fileExtension, long fileSizeKB) {
            document.filePath = filePath;
            document.fileExtension = fileExtension;
            document.fileSizeKB = fileSizeKB;
            return this;
        }

        public Builder withSignatureAndContent(String signature, String content) {
            document.digitalSignature = signature;
            document.extractedContent = content;
            return this;
        }

        public Builder withStatus(String status) {
            document.status = status;
            return this;
        }

        public Document build() {
            return document;
        }
    }
}