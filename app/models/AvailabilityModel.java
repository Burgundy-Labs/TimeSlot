package models;

import java.util.Date;

public class AvailabilityModel {
    private String availabilityId;
    private String userid;
    private Date startDate;
    private Date endDate;
    private Boolean weekly;
    private Boolean canBeWeekly; // Are not in DB
    private Boolean canBeOneTime; // Are not in DB

    public AvailabilityModel(){}

    public AvailabilityModel(String availabilityId, String userid, Date startDate, Date endDate, Boolean weekly) {
        this.availabilityId = availabilityId;
        this.userid = userid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekly = weekly;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getWeekly() {
        return weekly;
    }

    public void setWeekly(Boolean weekly) {
        this.weekly = weekly;
    }

    public String getavailabilityId() {
        return availabilityId;
    }

    public void setavailabilityId(String appointmentId) {
        this.availabilityId = appointmentId;
    }

    public Boolean getCanBeWeekly() {
        return canBeWeekly;
    }

    public void setCanBeWeekly(Boolean canBeWeekly) {
        this.canBeWeekly = canBeWeekly;
    }

    public Boolean getCanBeOneTime() {
        return canBeOneTime;
    }

    public void setCanBeOneTime(Boolean canBeOneTime) {
        this.canBeOneTime = canBeOneTime;
    }
}
