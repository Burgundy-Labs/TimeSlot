package controllers.ApplicationComponents;

import java.util.ArrayList;
import java.util.List;

public class AppointmentType {
    private static List<AppointmentType> appointmentTypes = new ArrayList<>();
    private String appointmentType;
    private String description;

    public AppointmentType(String appointmentType, String description) {
        this.appointmentType = appointmentType;
        this.description = description;
        appointmentTypes.add(this);
    }

    public static List<AppointmentType> getAppointmentTypes() {
        return appointmentTypes;
    }

    public static AppointmentType getAppointmentType(String appointmentType) {
        for(AppointmentType r : appointmentTypes) {
            if(r.appointmentType.equals(appointmentType)){
                return r;
            }
        }
        return null;
    }

    public String appointmentType() {
        return this.appointmentType;
    }
    public String description() {return this.description;}

    public void removeAppointmentType(AppointmentType appointmentType) {
        appointmentTypes.remove(appointmentType);
    }
}
