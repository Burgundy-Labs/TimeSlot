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

    public static Role getAppointmentType(String appointmentType) {
        return Role.valueOf(appointmentType.toUpperCase());
    }
}
