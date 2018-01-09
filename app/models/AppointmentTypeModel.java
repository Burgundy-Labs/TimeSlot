package models;

public class AppointmentTypeModel {
    private String appointmentTypeId;
    private String appointmentType;
    private Boolean oneTime;
    private Boolean weekly;

    public AppointmentTypeModel() {

    }

    public AppointmentTypeModel(String appointmentTypeId, String appointmentType) {
        this.appointmentTypeId = appointmentTypeId;
        this.appointmentType = appointmentType;
    }

    public AppointmentTypeModel(String appointmentTypeId, String appointmentType, Boolean weekly, Boolean oneTime) {
        this.appointmentTypeId = appointmentTypeId;
        this.appointmentType = appointmentType;
        this.weekly = weekly;
        this.oneTime = oneTime;
    }

    public String getAppointmentTypeId() {
        return appointmentTypeId;
    }

    public void setAppointmentTypeId(String appointmentTypeId) {
        this.appointmentTypeId = appointmentTypeId;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Boolean getOneTime() {
        return oneTime;
    }

    public void setOneTime(Boolean oneTime) {
        this.oneTime = oneTime;
    }

    public Boolean getWeekly() {
        return weekly;
    }

    public void setWeekly(Boolean weekly) {
        this.weekly = weekly;
    }
}
