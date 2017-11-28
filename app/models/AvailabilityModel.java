package models;

public class AvailabilityModel {
    private String availabilityId;
    private String userid;
    private String startDate;
    private String endDate;
    private Boolean weekly;

    public AvailabilityModel(){}

    public AvailabilityModel(String availabilityId, String userid, String startDate, String endDate, Boolean weekly) {
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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

    public void setavailabilityId(String availabilityId) {
        this.availabilityId = availabilityId;
    }
}
