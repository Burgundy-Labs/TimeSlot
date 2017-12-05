package models;

import controllers.ApplicationComponents.Roles;

public class AppointmentTypeModel {
    private String appointmentTypeId;
    private String appointmentType;

    public AppointmentTypeModel() {

    }

    public AppointmentTypeModel(String appointmentTypeId, String appointmentType) {
        this.appointmentTypeId = appointmentTypeId;
        this.appointmentType = appointmentType;
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
}
