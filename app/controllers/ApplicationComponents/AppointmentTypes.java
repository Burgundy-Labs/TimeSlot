package controllers.ApplicationComponents;

import java.util.ArrayList;
import java.util.List;

public class AppointmentTypes {
    private static List<AppointmentTypes> appointmentTypes = new ArrayList<>();
    private String appointmentType;
    private String description;

    public AppointmentTypes(String appointmentType, String description) {
        this.appointmentType = appointmentType;
        this.description = description;
        appointmentTypes.add(this);
    }

    public static List<AppointmentTypes> getAppointmentTypes() {
        return appointmentTypes;
    }

    public static AppointmentTypes getAppointmentType(String appointmentType) {
        for(AppointmentTypes r : appointmentTypes) {
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

    public void removeAppointmentType(AppointmentTypes appointmentType) {
        appointmentTypes.remove(appointmentType);
    }
}
