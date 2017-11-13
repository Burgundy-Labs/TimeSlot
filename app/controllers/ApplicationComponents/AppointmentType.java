package controllers.ApplicationComponents;

public enum AppointmentType {
    REGULAR("Regular"),
    ASYNC("Async"),
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
