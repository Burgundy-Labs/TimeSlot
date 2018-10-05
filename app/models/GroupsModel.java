package models;

import java.util.List;

public class GroupsModel {
    private String displayName; //Group's name
    private String photoURL; // Google-hosted profile photo
    private String uid; // Unique identifier provided by Google
    private String role; // "User", "Coach", "Admin"
    private List<String> attributes; // Used to store additional information
    /**
     * Not sure what data type this list should be in. Can't remember if it's
     * good practice to use models like this
    private List<UsersModel> usersList;
    **/


    public GroupsModel() { }

    public GroupsModel(String displayName, String photoURL, String uid, String role, List<String> attributes) {
        this.displayName = displayName;
        this.photoURL = photoURL;
        this.uid = uid;
        this.role = role;
        this.attributes = attributes;
    }

    public String getDisplayName() { return displayName; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getPhotoURL() { return photoURL; }

    public void setPhotoURL(String photoURL) { this.photoURL = photoURL; }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public List<String> getAttributes() { return attributes; }

    public void setAttributes(List<String> attributes) { this.attributes = attributes; }
}
