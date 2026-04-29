package vn.edu.tdtu.edocument;

import vn.edu.tdtu.edocument.model.Document;
import vn.edu.tdtu.edocument.service.DocumentProcessor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.UUID;

public class AddDocumentDialog extends JDialog {
    private JTextField txtApplicantName, txtApplicantEmail, txtApplicantPhone;
    private JTextField txtOfficerName, txtOfficerEmail, txtOfficerPhone;
    private JComboBox<String> cbDocumentType;
    private JTextField txtDigitalSignature;
    private JLabel lblFileName;
    private File selectedFile;
    
    private DocumentProcessor processor;
    private MainSwingUI parent;

    private Document existingDraft;

    public AddDocumentDialog(MainSwingUI parent, DocumentProcessor processor) {
        this(parent, processor, null);
    }

    public AddDocumentDialog(MainSwingUI parent, DocumentProcessor processor, Document existingDraft) {
        super(parent, existingDraft == null ? "Tiếp nhận hồ sơ mới" : "Tiếp tục tạo hồ sơ nháp", true);
        this.parent = parent;
        this.processor = processor;
        this.existingDraft = existingDraft;
        
        setSize(450, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 5, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Tên người nộp:"));
        txtApplicantName = new JTextField();
        formPanel.add(txtApplicantName);

        formPanel.add(new JLabel("Email người nộp:"));
        txtApplicantEmail = new JTextField();
        formPanel.add(txtApplicantEmail);

        formPanel.add(new JLabel("SĐT người nộp:"));
        txtApplicantPhone = new JTextField();
        formPanel.add(txtApplicantPhone);

        formPanel.add(new JLabel("Tên cán bộ:"));
        txtOfficerName = new JTextField("Cán bộ trực ban");
        formPanel.add(txtOfficerName);

        formPanel.add(new JLabel("Email cán bộ:"));
        txtOfficerEmail = new JTextField("officer@tdtu.edu.vn");
        formPanel.add(txtOfficerEmail);

        formPanel.add(new JLabel("SĐT cán bộ:"));
        txtOfficerPhone = new JTextField("0123456789");
        formPanel.add(txtOfficerPhone);

        formPanel.add(new JLabel("Loại hồ sơ:"));
        cbDocumentType = new JComboBox<>(new String[]{"DON_XIN_PHEP", "BAO_CAO", "HO_SO_THUE"});
        formPanel.add(cbDocumentType);

        formPanel.add(new JLabel("Chữ ký số:"));
        txtDigitalSignature = new JTextField();
        formPanel.add(txtDigitalSignature);

        formPanel.add(new JLabel("Tập tin đính kèm:"));
        JButton btnFile = new JButton("Chọn...");
        lblFileName = new JLabel("Chưa chọn");
        JPanel pFile = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pFile.add(btnFile); pFile.add(lblFileName);
        formPanel.add(pFile);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSaveDraft = new JButton("Lưu nháp");
        JButton btnSend = new JButton("Gửi");
        JButton btnCancel = new JButton("Hủy");
        btnPanel.add(btnSaveDraft);
        btnPanel.add(btnSend);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        if (existingDraft != null) {
            txtApplicantName.setText(existingDraft.applicantName);
            txtApplicantEmail.setText(existingDraft.applicantEmail);
            txtApplicantPhone.setText(existingDraft.applicantPhone);
            txtOfficerName.setText(existingDraft.officerName);
            txtOfficerEmail.setText(existingDraft.officerEmail);
            txtOfficerPhone.setText(existingDraft.officerPhone);
            cbDocumentType.setSelectedItem(existingDraft.documentType);
            txtDigitalSignature.setText(existingDraft.digitalSignature);
            if (existingDraft.filePath != null && !existingDraft.filePath.isEmpty()) {
                selectedFile = new File(existingDraft.filePath);
                lblFileName.setText(selectedFile.getName());
            }
        }

        btnFile.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fc.getSelectedFile();
                lblFileName.setText(selectedFile.getName());
            }
        });

        btnCancel.addActionListener(e -> dispose());

        btnSaveDraft.addActionListener(e -> saveDraftAction());

        btnSend.addActionListener(e -> submitAction());
    }

    private Document.Builder createBuilderFromForm() {
        String filePath = (selectedFile != null) ? selectedFile.getAbsolutePath() : "";
        String ext = "";
        long size = 0;
        if (selectedFile != null) {
            size = selectedFile.length() / 1024;
            String name = selectedFile.getName();
            int lastDot = name.lastIndexOf('.');
            if (lastDot > 0) {
                ext = name.substring(lastDot + 1);
            }
        }

        String docId = (existingDraft != null) ? existingDraft.id : UUID.randomUUID().toString().substring(0, 8);

        return new Document.Builder()
            .withId(docId)
            .withApplicantInfo(txtApplicantName.getText().trim(), txtApplicantEmail.getText().trim(), txtApplicantPhone.getText().trim())
            .withOfficerInfo(txtOfficerName.getText().trim(), txtOfficerEmail.getText().trim(), txtOfficerPhone.getText().trim())
            .withDocumentType(cbDocumentType.getSelectedItem().toString())
            .withFileInfo(filePath, ext, size)
            .withSignatureAndContent(txtDigitalSignature.getText().trim(), null);
    }

    private void saveDraftAction() {
        Document draftDoc = createBuilderFromForm()
            .withStatus("NHAP")
            .build();
            
        System.out.println("[HỆ THỐNG] Đã lưu nháp hồ sơ: " + draftDoc.id);
        
        if (existingDraft != null) {
            parent.updateDocumentInList(existingDraft, draftDoc);
        } else {
            parent.addDocumentToList(draftDoc);
        }
        dispose();
    }

    private void submitAction() {
        Document doc = createBuilderFromForm()
            .withStatus("MOI_TAO")
            .build();

        processor.process(doc);

        if ("DANG_XET_DUYET".equals(doc.status)) {
            if (existingDraft != null) {
                parent.updateDocumentInList(existingDraft, doc);
            } else {
                parent.addDocumentToList(doc);
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Hồ sơ không hợp lệ. Vui lòng kiểm tra lại log.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}