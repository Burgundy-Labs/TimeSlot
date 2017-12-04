package models;

import java.util.Date;

public class NotificationModel {
    private String notificationId;
    private String notificationContent;
    private Date creationDate;

    public NotificationModel() {

    }

    public NotificationModel(String notificationId, String notificationContent) {
       this.notificationId = notificationId;
       this.notificationContent = notificationContent;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
