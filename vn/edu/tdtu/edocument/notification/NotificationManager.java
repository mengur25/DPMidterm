package vn.edu.tdtu.edocument.notification;

import vn.edu.tdtu.edocument.model.Document;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
    private List<NotificationObserver> observers = new ArrayList<>();

    public void subscribe(NotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unsubscribe(NotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyAll(Document document) {
        for (NotificationObserver observer : observers) {
            observer.update(document);
        }
    }
}
