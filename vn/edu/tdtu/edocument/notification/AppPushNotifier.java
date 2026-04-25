package vn.edu.tdtu.edocument.notification;

import vn.edu.tdtu.edocument.model.Document;

public class AppPushNotifier implements NotificationObserver {
    @Override
    public void update(Document document) {
        System.out.println("  [APP PUSH] -> Gửi thông báo đẩy về App cho hồ sơ [" + document.id + "], trạng thái: " + document.status);
    }
}
