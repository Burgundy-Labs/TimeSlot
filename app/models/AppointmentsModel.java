package models;
import controllers.ApplicationComponents.AppointmentType;

public class AppointmentsModel {
    private String appointmentId;
    private String startDate;
    private String endDate;
    private String studentId;
    private String coachId;
    private String appointmentNotes;
    private boolean present;
    private AppointmentType appointmentType;

    public AppointmentsModel( ) {

    }

    public AppointmentsModel(
            String appointmentId,
            String startDate,
            String endDate,
            String studentName,
            String coachName,
            String appointmentNotes,
            boolean present,
            AppointmentType appointmentType ) {
        this.appointmentId = appointmentId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studentId = studentName;
        this.coachId = coachName;
        this.appointmentNotes = appointmentNotes;
        this.present = present;
        this.appointmentType = appointmentType;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getAppointmentNotes() {
        return appointmentNotes;
    }

    public void setAppointmentNotes(String appointmentNotes) {
        this.appointmentNotes = appointmentNotes;
    }

    public boolean getPresent() { return present; }

    public void setPresent(boolean present) { this.present = present; }

    public AppointmentType getAppointmentType() { return appointmentType; }

    public void setAppointmentType( AppointmentType appointmentType ) { this.appointmentType = appointmentType; }
}
