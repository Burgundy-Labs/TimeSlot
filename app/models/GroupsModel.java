package models;

import java.util.Date;
import java.util.List;
import java.time.*;

public class GroupsModel {
    private List<String> coaches; //Coach names
    private List<String> student; //Student names
    private List<String> requestJoin; //People wanting to join
    private String groupID; //Group ID
    private List<DayOfWeek> recur; //Each day the appointment recurs
    private String groupDesc; //Group appointment description
    private int capacity; //Number of students that can join the appointment
    private LocalDate startDate; //Day that recurring appointments start
    private LocalDate endDate; //Day that recurring appointments end
    private LocalTime startTime; //Time during the day that the appointment occurs
    private LocalTime endTime; //Time during the day that the appointment ends
    private String groupCategory; //Group appointment type
    private String location; //Location of appointment
    public boolean isPublic; //Whether the appointment is open to public

    public class GroupInstanceModel {
        String coachNotes;  //The coach's notes for the meeting
        String instanceID;  //The id of the instance
        String groupID;
        LocalDateTime dateStamp;
        LocalDateTime dateStampEnd;
        List<String> presentIDs;    //The list of ids of those who are present

        public GroupInstanceModel(String coachNotes, String instanceID,
                                  List<String> presentIDs, String groupID,
                                  LocalDateTime dateStamp, LocalDateTime dateStampEnd) {
            this.coachNotes = coachNotes;
            this.instanceID = instanceID;
            this.presentIDs = presentIDs;
            this.groupID = groupID;
            this.dateStamp = dateStamp;
            this.dateStampEnd = dateStampEnd;
        }

        public String getGroupID() {return groupID;}

        public void setGroupID(String groupID) {
            this.groupID = groupID;
        }

        public LocalDateTime getDateStamp() { return dateStamp;}

        public void setDateStamp(LocalDateTime dateStamp) {
            this.dateStamp = dateStamp;
        }

        public LocalDateTime getDateStampEnd() { return dateStampEnd; }

        public void setDateStampEnd(LocalDateTime dateStampEnd) {
            this.dateStampEnd = dateStampEnd;
        }

        public String getCoachNotes() {
            return coachNotes;
        }

        public void setCoachNotes(String coachNotes) {
            this.coachNotes = coachNotes;
        }

        public String getInstanceID() {
            return instanceID;
        }

        public void setInstanceID(String instanceID) {
            this.instanceID = instanceID;
        }

        public List<String> getPresentIDs() {
            return presentIDs;
        }

        public void setPresentIDs(List<String> presentIDs) {
            this.presentIDs = presentIDs;
        }
    }

    public GroupsModel(){

    }

    public GroupsModel(List<String> coaches,
                       List<String> student,
                       List<String> requestJoin,
                       String groupID,
                       List<DayOfWeek> recur,
                       String groupDesc,
                       int capacity,
                       LocalDate startDate,
                       LocalDate endDate,
                       LocalTime startTime,
                       LocalTime endTime,
                       String groupCategory,
                       String location,
                       boolean isPublic) {
        this.coaches = coaches;
        this.student = student;
        this.requestJoin = requestJoin;
        this.groupID = groupID;
        this.recur = recur;
        this.groupDesc = groupDesc;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.groupCategory = groupCategory;
        this.location = location;
        this.isPublic = isPublic;
    }

    public void addStudent(String studentID){
        student.add(studentID);
    }

    public void leaveGroup(String ID){
        if(student.contains(ID)){
            student.remove(ID);
        } else if(coaches.contains(ID)){
            coaches.remove(ID);
        }
    }



    public List<String> getCoaches() {
        return coaches;
    }

    public void setCoaches(List<String> coaches) {
        this.coaches = coaches;
    }

    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }

    public List<String> getRequestJoin() {
        return requestJoin;
    }

    public void setRequestJoin(List<String> requestJoin) {
        this.requestJoin = requestJoin;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public List<DayOfWeek> getRecur() {
        return recur;
    }

    public void setRecur(List<DayOfWeek> recur) {
        this.recur = recur;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getGroupCategory() {
        return groupCategory;
    }

    public void setGroupCategory(String groupCategory) {
        this.groupCategory = groupCategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
