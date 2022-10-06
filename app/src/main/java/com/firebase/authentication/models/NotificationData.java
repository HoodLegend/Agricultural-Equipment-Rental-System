package com.firebase.authentication.models;

public class NotificationData {
    private String title;
    private String message;
    private String type;
    private String equipment;

    public NotificationData(String title, String message, String type) {
        this.title = title;
        this.message = message;
        this.type = type;
    }

    public NotificationData(String title, String message, String type, String  equipment) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.equipment = equipment;
    }


    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public NotificationData (){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
