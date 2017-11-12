package controllers.ApplicationComponents;

public enum AppointmentType {
    ONE_TO_ONE("One-to-one"),
    ASYNC("Async"),
    DEFAULT(ONE_TO_ONE.appointmentType);

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
