package models;

public class AppointmentsModel {
    private String appointmentId;
    private String startDate;
    private String endDate;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhoto;
    private String coachId;
    private String coachName;
    private String coachEmail;
    private String coachPhoto;
    private String appointmentNotes;
    private String coachNotes;
    private boolean present;
    private String appointmentType;

    public AppointmentsModel( ) {

    }

    public AppointmentsModel(
            String appointmentId,
            String startDate,
            String endDate,
            String studentId,
            String studentName,
            String studentEmail,
            String studentPhoto,
            String coachId,
            String coachName,
            String coachEmail,
            String coachPhoto,
            String appointmentNotes,
            String coachNotes,
            boolean present,
            String appointmentType ) {
        this.appointmentId = appointmentId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studentName = studentName;
        this.studentId = studentId;
        this.studentEmail = studentEmail;
        this.studentPhoto = studentPhoto;
        this.coachId = coachId;
        this.coachName = coachName;
        this.coachEmail = coachEmail;
        this.coachPhoto = coachPhoto;
        this.appointmentNotes = appointmentNotes;
        this.coachNotes = coachNotes;
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

    public String getAppointmentType() { return appointmentType; }

    public void setAppointmentType( String appointmentType ) { this.appointmentType = appointmentType; }

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

    public String getCoachEmail() {
        return coachEmail;
    }

    public void setCoachEmail(String coachEmail) {
        this.coachEmail = coachEmail;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getCoachPhoto() {
        return coachPhoto;
    }

    public void setCoachPhoto(String coachPhoto) {
        this.coachPhoto = coachPhoto;
    }

    public String getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    public String getCoachNotes() {
        return coachNotes;
    }

    public void setCoachNotes(String coachNotes) {
        this.coachNotes = coachNotes;
    }
}
