package models;
import java.util.Date;
//should setters pass info back to database (or however that works)

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
