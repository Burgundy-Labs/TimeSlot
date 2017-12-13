package models;

import java.util.Date;

public class SettingsModel {
    private String universityName;
    private String centerName;
    private Date semesterStart;
    private Date semesterEnd;
    private Date startTime;
    private Date endTime;

    public static SettingsModel replaceNull(SettingsModel settings) {
        if(settings == null) settings = new SettingsModel();
        if(settings.getUniversityName() == null) settings.setUniversityName("NOT SET");
        if(settings.getCenterName() == null) settings.setCenterName("NOT SET");
        /* Verify that color exists - if not set to white and black */
        if(settings.getSemesterStart() == null) settings.setSemesterStart(new Date());
        if(settings.getSemesterEnd() == null) settings.setSemesterEnd(new Date());
        if(settings.getStartTime() == null) settings.setStartTime(new Date());
        if(settings.getEndTime() == null) settings.setEndTime(new Date());
        return settings;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getCenterName() { return centerName; }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public Date getSemesterStart() { return semesterStart; }

    public void setSemesterStart(Date semesterStart) { this.semesterStart = semesterStart; }

    public Date getSemesterEnd() { return semesterEnd; }

    public void setSemesterEnd(Date semesterEnd) { this.semesterEnd = semesterEnd; }

    public Date getStartTime() { return startTime; }

    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }

    public void setEndTime(Date endTime) { this.endTime = endTime; }
}
