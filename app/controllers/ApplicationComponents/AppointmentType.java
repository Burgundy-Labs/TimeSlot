package controllers.ApplicationComponents;

public enum AppointmentType {
    REGULAR("Regular"),
    WEEKLY("Weekly"),
    ONLINE("Online"),
    DEFAULT(REGULAR.appointmentType);

    private String appointmentType;

    AppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String appointmentType() {
        return appointmentType;
    }

    public static AppointmentType getAppointmentType(String appointmentType) {
        return AppointmentType.valueOf(appointmentType.toUpperCase());
    }
}
