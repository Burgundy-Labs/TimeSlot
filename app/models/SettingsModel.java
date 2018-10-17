package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SettingsModel {
    private String universityName;
    private String centerName;
    private Date semesterStart;
    private Date semesterEnd;
    private Date startTime;
    private Date endTime;
    private String siteAlert;
    private String centerInformation;
    private Integer maximumAppointments;
    private List<Boolean> daysOpenWeekly;

    public static SettingsModel replaceNull(SettingsModel settings) {
        if(settings == null) settings = new SettingsModel();
        if(settings.getUniversityName() == null) settings.setUniversityName("CHANGE IN SETTINGS");
        if(settings.getCenterName() == null) settings.setCenterName("CHANGE IN SETTINGS");
        /* Verify that color exists - if not set to white and black */
        if(settings.getSemesterStart() == null) settings.setSemesterStart(new Date());
        if(settings.getSemesterEnd() == null) settings.setSemesterEnd(new Date());
        if(settings.getStartTime() == null) settings.setStartTime(new Date());
        if(settings.getEndTime() == null) settings.setEndTime(new Date());
        if(settings.getSiteAlert() == null) settings.setSiteAlert("");
        if(settings.getCenterInformation() == null) settings.setCenterInformation("");
        if(settings.getMaximumAppointments() == null) settings.setMaximumAppointments(5);
        if(settings.getDaysOpenWeekly() == null) settings.setDaysOpenWeekly(new ArrayList<>(Arrays.asList(true,true,true,true,true,true,true)));
        return settings;
    }

    public Integer getMaximumAppointments() { if (maximumAppointments == null) {maximumAppointments = 5;} return maximumAppointments; }

    public void setMaximumAppointments(Integer maximumAppointments) { this.maximumAppointments = maximumAppointments; }

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

    public String getSiteAlert() {
        return siteAlert;
    }

    public void setSiteAlert(String siteAlert) {
        this.siteAlert = siteAlert;
    }

    public String getCenterInformation() {
        return centerInformation;
    }

    public void setCenterInformation(String centerInformation) { this.centerInformation = centerInformation; }

    public List<Boolean> getDaysOpenWeekly() {
        if ( daysOpenWeekly == null ) {
            daysOpenWeekly = new ArrayList<>(Arrays.asList(true,true,true,true,true,true,true));
        }
        return daysOpenWeekly;
    }

    public void setDaysOpenWeekly(List<Boolean> days) { daysOpenWeekly = days; }

}
