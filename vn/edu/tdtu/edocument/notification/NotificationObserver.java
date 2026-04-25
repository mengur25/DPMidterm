package vn.edu.tdtu.edocument.notification;

import vn.edu.tdtu.edocument.model.Document;

public interface NotificationObserver {
    void update(Document document);
}
