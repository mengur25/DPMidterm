package vn.edu.tdtu.edocument.storage;

import vn.edu.tdtu.edocument.model.Document;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class LocalJsonStorage implements DocumentStorage {
    @Override
    public void save(Document doc) {
        String storageDirPath = "server_storage";
        File storageDir = new File(storageDirPath);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        try {
            Path sourcePath = Paths.get(doc.filePath);
            Path targetPath = Paths.get(storageDirPath + File.separator + doc.id + "_" + sourcePath.getFileName().toString());
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            String json = "{\n" +
                    "  \"id\": \"" + doc.id + "\",\n" +
                    "  \"applicantName\": \"" + doc.applicantName + "\",\n" +
                    "  \"applicantEmail\": \"" + doc.applicantEmail + "\",\n" +
                    "  \"applicantPhone\": \"" + doc.applicantPhone + "\",\n" +
                    "  \"officerName\": \"" + doc.officerName + "\",\n" +
                    "  \"officerEmail\": \"" + doc.officerEmail + "\",\n" +
                    "  \"officerPhone\": \"" + doc.officerPhone + "\",\n" +
                    "  \"documentType\": \"" + doc.documentType + "\",\n" +
                    "  \"filePath\": \"" + targetPath.toString().replace("\\", "\\\\") + "\",\n" +
                    "  \"fileExtension\": \"" + doc.fileExtension + "\",\n" +
                    "  \"fileSizeKB\": " + doc.fileSizeKB + ",\n" +
                    "  \"digitalSignature\": \"" + doc.digitalSignature + "\",\n" +
                    "  \"status\": \"" + doc.status + "\"\n" +
                    "}";

            File dataFile = new File(storageDirPath + File.separator + doc.id + "_data.json");
            FileWriter writer = new FileWriter(dataFile);
            writer.write(json);
            writer.close();
            System.out.println("  [LƯU TRỮ] -> Đã lưu hồ sơ vào Local JSON Storage.");

        } catch (IOException e) {
            System.out.println("[LỖI HỆ THỐNG] Lỗi khi lưu trữ vật lý: " + e.getMessage());
        }
    }
}
