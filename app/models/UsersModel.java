package models;

public class UsersModel {
    private String displayName;
    private String email;
    private String photoURL;
    private String uid;
    private String role;
    private String auth_id;

    public UsersModel() {

    }

    public UsersModel(String displayName, String email, String photoURL, String uid, String role, String auth_id) {
        this.displayName = displayName;
        this.email = email;
        this.photoURL = photoURL;
        this.uid = uid;
        this.role = role;
        this.auth_id = auth_id;
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
}
