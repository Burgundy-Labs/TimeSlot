package models;

import java.util.Arrays;

public class UsersModel {
    private String displayName; // User's full name
    private String email;
    private String photoURL; // Google-hosted profile photo
    private String uid; // Unique identifier provided by Google
    private String role; // "User", "Coach", "Admin"
    private String auth_id; // Used to identify RFID / Phone sign-in
    private boolean subscribed; // bool for sending email reminders
    private String[] attributes; // Used to store additional information ("isCoach")

    public UsersModel() {

    }

    public UsersModel(String displayName, String email, String photoURL, String uid, String role, String auth_id, String[] attributes, boolean subscribed) {
        this.displayName = displayName;
        this.email = email;
        this.photoURL = photoURL;
        this.uid = uid;
        this.role = role;
        this.auth_id = auth_id;
        this.attributes = attributes;
        this.subscribed = subscribed;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String ID) {
        this.auth_id = ID;
    }

    public String[] getAttributes() {
        return attributes == null ? new String[]{} : attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }


    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
