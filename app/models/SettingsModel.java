package models;

public class SettingsModel {
    private String universityName;
    private String centerName;
    private String primaryColor;
    private String secondaryColor;

    public static SettingsModel replaceNull(SettingsModel settings) {
        if(settings == null) settings = new SettingsModel();
        if(settings.getUniversityName() == null) settings.setUniversityName("NOT SET");
        if(settings.getCenterName() == null) settings.setCenterName("NOT SET");
        /* Verify that color exists - if not set to white and black */
        if(settings.getPrimaryColor() == null) settings.setPrimaryColor("#000000");
        if(settings.getSecondaryColor() == null) settings.setSecondaryColor("#FFFFFF");
        return settings;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }
}
