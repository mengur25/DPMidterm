package vn.edu.tdtu.edocument.notification;

import vn.edu.tdtu.edocument.model.Document;

public class EmailNotifier implements NotificationObserver {
    @Override
    public void update(Document document) {
        System.out.println("  [GỬI EMAIL] -> Trạng thái hồ sơ [" + document.id + "] đã chuyển sang: " + document.status);
        if (document.applicantEmail != null && !document.applicantEmail.isEmpty()) {
            System.out.println("      + Tới người nộp: " + document.applicantEmail);
        }
        if (document.officerEmail != null && !document.officerEmail.isEmpty()) {
            System.out.println("      + Tới cán bộ: " + document.officerEmail);
        }
    }
}
