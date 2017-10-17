package models;

//should setters pass info back to database (or however that works)

public class AppointmentsModel {
    private String appointmentName;
    private String appointmentId;
    // want to use Calendar instead of Date
    private String startDate;
    private String endDate;
    private String studentName;
    private String coachName;
    private String appointmentNotes;

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

    //should this just be what the constructor does?
    public void loadData(JSON data) {
//      Data should be in the form: { "appointmentId":String, "startDate":String, "endDate":String, "studentName":String, "coachName":String, "appointmentNotes":String }

        appointmentName = (String) data.get("appointmentName");
        appointmentId = (String) data.get("appointmentId");
        studentName = (String) data.get("studentName");
        coachName = (String) data.get("coachName");
        appointmentNotes = (String) data.get("appointmentNotes");
        startDate = (String) data.get("startDate");
        endDate = (String) data.get("endDate");

        //How will date data be passed with JSON???

    }

}
