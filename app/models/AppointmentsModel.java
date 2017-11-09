package models;
import java.util.Date;

public class AppointmentsModel {
    private String appointmentName;
    private String appointmentId;
    private Date startDate;
    private Date endDate;
    private String studentName;
    private String coachName;
    private String appointmentNotes;
    private boolean present;
    private String appointmentType;

    public AppointmentsModel( ) {

    }

    public AppointmentsModel(
            String appointmentName,
            String appointmentId,
            Date startDate,
            Date endDate,
            String studentName,
            String coachName,
            String appointmentNotes,
            boolean present,
            String appointmentType ) {
        this.appointmentName = appointmentName;
        this.appointmentId = appointmentId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studentName = studentName;
        this.coachName = coachName;
        this.appointmentNotes = appointmentNotes;
        this.present = present;
        this.appointmentType = appointmentType;
    }

    public String getAppointmentName() { return appointmentName; }

    public void setAppointmentName(String appointmentName) { this.appointmentName = appointmentName; }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getAppointmentNotes() {
        return appointmentNotes;
    }

    public void setAppointmentNotes(String appointmentNotes) {
        this.appointmentNotes = appointmentNotes;
    }

    public boolean getPresent() { return present; }

    public void setPresent(boolean present) { this.present = present; }

    public String getAppointmentType() { return appointmentType; }

    public void setAppointmentType( String appointmentType ) { this.appointmentType = appointmentType; }
}
