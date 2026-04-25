package vn.edu.tdtu.edocument.notification;

import vn.edu.tdtu.edocument.model.Document;

public class SMSNotifier implements NotificationObserver {
    @Override
    public void update(Document document) {
        System.out.println("  [GỬI SMS] -> Trạng thái hồ sơ [" + document.id + "] đã chuyển sang: " + document.status);
        if (document.applicantPhone != null && !document.applicantPhone.isEmpty()) {
            System.out.println("      + Tới số ĐT người nộp: " + document.applicantPhone);
        }
        if (document.officerPhone != null && !document.officerPhone.isEmpty()) {
            System.out.println("      + Tới số ĐT cán bộ: " + document.officerPhone);
        }
    }
}
