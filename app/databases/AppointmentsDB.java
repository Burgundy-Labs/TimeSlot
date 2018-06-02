package databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import models.AppointmentsModel;
import models.UsersModel;

import java.util.*;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
* AppointmentsDB works with AppointmentsModel to retrieve and remove appointments in the Firestore DB.*/
/* TODO implement as DBInterface */
public class AppointmentsDB {
    private UserDB userDB = new UserDB();

    public  AppointmentsModel getAppointment(String appointmentId) {
        /* Return null appointment if none found */
        AppointmentsModel appointmentFound = null;
        /* Get the specific appointment reference from the DB*/
        DocumentReference docRef = FirestoreHandler.get().collection("appointments").document(appointmentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        try {
            /* Attempt to get the reference - blocking */
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert document != null;
        if (document.exists()) {
            appointmentFound = new AppointmentsModel(
                    document.getId(),
                    document.getDate("start_date"),
                    document.getDate("end_date"),
                    document.getString("studentId"),
                    document.getString("studentName"),
                    document.getString("studentEmail"),
                    document.getString("studentPhoto"),
                    document.getString("coachId"),
                    document.getString("coachName"),
                    document.getString("coachEmail"),
                    document.getString("coachPhoto"),
                    document.getString("appointment_notes"),
                    document.getString("coach_notes"),
                    document.getBoolean("present"),
                    document.getString("appointment_type"),
                    document.getString("service_type"),
                    document.getBoolean("weekly"),
                    document.getString("weeklyId"));
        } else {
            /* Log something */
        }
        return appointmentFound;
    }

    public List<AppointmentsModel> getAppointmentsForUser(String role, String userId) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> coachQuery = FirestoreHandler.get().collection("appointments").orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("coachId",userId).get();
        ApiFuture<QuerySnapshot> studentQuery = FirestoreHandler.get().collection("appointments").orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("studentId",userId).get();

        QuerySnapshot querySnapshotCoach = null;
        QuerySnapshot querySnapshotStudent = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshotCoach = coachQuery.get();
            querySnapshotStudent = studentQuery.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshotCoach != null;
        assert querySnapshotStudent != null;
        List<QueryDocumentSnapshot> documentsCoach = querySnapshotCoach.getDocuments();
        List<QueryDocumentSnapshot> documentsStudent = querySnapshotStudent.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documentsCoach) {
            AppointmentsModel appointment = new AppointmentsModel(
                    document.getId(),
                    document.getDate("start_date"),
                    document.getDate("end_date"),
                    document.getString("studentId"),
                    document.getString("studentName"),
                    document.getString("studentEmail"),
                    document.getString("studentPhoto"),
                    document.getString("coachId"),
                    document.getString("coachName"),
                    document.getString("coachEmail"),
                    document.getString("coachPhoto"),
                    document.getString("appointment_notes"),
                    document.getString("coach_notes"),
                    document.getBoolean("present"),
                    document.getString("appointment_type"),
                    document.getString("service_type"),
                    document.getBoolean("weekly"),
                    document.getString("weeklyId"));
            appointmentList.add(appointment);
        }
        for (DocumentSnapshot document : documentsStudent) {
            AppointmentsModel appointment = new AppointmentsModel(
                    document.getId(),
                    document.getDate("start_date"),
                    document.getDate("end_date"),
                    document.getString("studentId"),
                    document.getString("studentName"),
                    document.getString("studentEmail"),
                    document.getString("studentPhoto"),
                    document.getString("coachId"),
                    document.getString("coachName"),
                    document.getString("coachEmail"),
                    document.getString("coachPhoto"),
                    document.getString("appointment_notes"),
                    document.getString("coach_notes"),
                    document.getBoolean("present"),
                    document.getString("appointment_type"),
                    document.getString("service_type"),
                    document.getBoolean("weekly"),
                    document.getString("weeklyId"));
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public List<AppointmentsModel> getNextFiveAppointmentsForUser(String role, String userId) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> coachQuery = FirestoreHandler.get().collection("appointments").whereGreaterThan("start_date",new Date()).orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("coachId",userId).limit(5).get();
        ApiFuture<QuerySnapshot> studentQuery = FirestoreHandler.get().collection("appointments").whereGreaterThan("start_date",new Date()).orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("studentId",userId).limit(5).get();
        QuerySnapshot querySnapshotCoach = null;
        QuerySnapshot querySnapshotStudent = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshotCoach = coachQuery.get();
            querySnapshotStudent = studentQuery.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshotCoach != null;
        assert querySnapshotStudent != null;
        List<QueryDocumentSnapshot> documentsCoach = querySnapshotCoach.getDocuments();
        List<QueryDocumentSnapshot> documentsStudent = querySnapshotStudent.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documentsCoach) {
            AppointmentsModel appointment = new AppointmentsModel(
                    document.getId(),
                    document.getDate("start_date"),
                    document.getDate("end_date"),
                    document.getString("studentId"),
                    document.getString("studentName"),
                    document.getString("studentEmail"),
                    document.getString("studentPhoto"),
                    document.getString("coachId"),
                    document.getString("coachName"),
                    document.getString("coachEmail"),
                    document.getString("coachPhoto"),
                    document.getString("appointment_notes"),
                    document.getString("coach_notes"),
                    document.getBoolean("present"),
                    document.getString("appointment_type"),
                    document.getString("service_type"),
                    document.getBoolean("weekly"),
                    document.getString("weeklyId"));
            appointmentList.add(appointment);
        }
        for (DocumentSnapshot document : documentsStudent) {
            AppointmentsModel appointment = new AppointmentsModel(
                    document.getId(),
                    document.getDate("start_date"),
                    document.getDate("end_date"),
                    document.getString("studentId"),
                    document.getString("studentName"),
                    document.getString("studentEmail"),
                    document.getString("studentPhoto"),
                    document.getString("coachId"),
                    document.getString("coachName"),
                    document.getString("coachEmail"),
                    document.getString("coachPhoto"),
                    document.getString("appointment_notes"),
                    document.getString("coach_notes"),
                    document.getBoolean("present"),
                    document.getString("appointment_type"),
                    document.getString("service_type"),
                    document.getBoolean("weekly"),
                    document.getString("weeklyId"));
            appointmentList.add(appointment);
        }
        appointmentList.sort(Comparator.comparing(AppointmentsModel::getStartDate));
        if ( appointmentList.size() > 5 ) {
            appointmentList = appointmentList.subList(0, 5);
        }
        return appointmentList;
    }

    public List<AppointmentsModel> getAppointmentsByUserAndDate(String userId, Date start, Date end) {
        UsersModel user = userDB.get(userId);
        List<AppointmentsModel> appointmentList = getAppointmentsByDate(start, end);
        if( user.getRole().equals("Coach")){
            appointmentList.removeIf(i -> !i.getCoachId().equals(user.getUid()) && !i.getStudentId().equals(user.getUid()));
        } else {
            appointmentList.removeIf(i -> !i.getStudentId().equals(user.getUid()));
        }
        return appointmentList;
    }

    public List<AppointmentsModel> getAppointmentsByDate(Date start, Date end) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").orderBy("start_date", Query.Direction.ASCENDING).whereGreaterThanOrEqualTo("start_date",start).get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documents) {
            if(document.getDate("end_date").before(end)){
            AppointmentsModel appointment = new AppointmentsModel(
                    document.getId(),
                    document.getDate("start_date"),
                    document.getDate("end_date"),
                    document.getString("studentId"),
                    document.getString("studentName"),
                    document.getString("studentEmail"),
                    document.getString("studentPhoto"),
                    document.getString("coachId"),
                    document.getString("coachName"),
                    document.getString("coachEmail"),
                    document.getString("coachPhoto"),
                    document.getString("appointment_notes"),
                    document.getString("coach_notes"),
                    document.getBoolean("present"),
                    document.getString("appointment_type"),
                    document.getString("service_type"),
                    document.getBoolean("weekly"),
                    document.getString("weeklyId"));
            appointmentList.add(appointment);
            } else {
                break;
            }
        }
        return appointmentList;
    }


    public List<AppointmentsModel> getWeeklyAppointmentsByWeeklyId(String weeklyId) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").whereEqualTo("weeklyId", weeklyId).get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documents) {
                AppointmentsModel appointment = new AppointmentsModel(
                        document.getId(),
                        document.getDate("start_date"),
                        document.getDate("end_date"),
                        document.getString("studentId"),
                        document.getString("studentName"),
                        document.getString("studentEmail"),
                        document.getString("studentPhoto"),
                        document.getString("coachId"),
                        document.getString("coachName"),
                        document.getString("coachEmail"),
                        document.getString("coachPhoto"),
                        document.getString("appointment_notes"),
                        document.getString("coach_notes"),
                        document.getBoolean("present"),
                        document.getString("appointment_type"),
                        document.getString("service_type"),
                        document.getBoolean("weekly"),
                        document.getString("weeklyId"));
                appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public List<AppointmentsModel> getAppointments() {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documents) {
            AppointmentsModel appointment = new AppointmentsModel(
                    document.getId(),
                    document.getDate("start_date"),
                    document.getDate("end_date"),
                    document.getString("studentId"),
                    document.getString("studentName"),
                    document.getString("studentEmail"),
                    document.getString("studentPhoto"),
                    document.getString("coachId"),
                    document.getString("coachName"),
                    document.getString("coachEmail"),
                    document.getString("coachPhoto"),
                    document.getString("appointment_notes"),
                    document.getString("coach_notes"),
                    document.getBoolean("present"),
                    document.getString("appointment_type"),
                    document.getString("service_type"),
                    document.getBoolean("weekly"),
                    document.getString("weeklyId"));
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public AppointmentsModel addAppointment(AppointmentsModel appointment) {
        /* Get DB instance */
        DocumentReference docRef;
        if(appointment.getAppointmentId() == null) {
            docRef = FirestoreHandler.get().collection("appointments").document();
        } else {
            docRef = FirestoreHandler.get().collection("appointments").document(appointment.getAppointmentId());
        }
        Map<String, Object> data = new HashMap<>();
        /* Create user model for DB insert */
        UsersModel student = userDB.get(appointment.getStudentId());
        UsersModel coach = userDB.get(appointment.getCoachId());
        data.put("start_date", appointment.getStartDate());
        data.put("end_date", appointment.getEndDate());
        data.put("studentId", appointment.getStudentId());
        data.put("studentName",student.getDisplayName());
        data.put("studentEmail", student.getEmail());
        data.put("studentPhoto", student.getPhotoURL());
        data.put("coachId", appointment.getCoachId());
        data.put("coachName", coach.getDisplayName());
        data.put("coachEmail", coach.getEmail());
        data.put("coachPhoto", coach.getPhotoURL());
        data.put("appointment_notes", appointment.getAppointmentNotes());
        data.put("coach_notes", appointment.getCoachNotes() != null ? appointment.getCoachNotes() : "");
        data.put("present", appointment.getPresent());
        data.put("appointment_type", appointment.getAppointmentType());
        data.put("service_type",appointment.getServiceType());
        data.put("weekly", appointment.isWeekly());
        data.put("weeklyId", appointment.getWeeklyId());
        /* Asynchronously write appointment into DB */
        ApiFuture<WriteResult> result = docRef.set(data);

        /* Add required email information to appointment model for emails */
        appointment.setCoachName(coach.getDisplayName());
        appointment.setCoachEmail(coach.getEmail());
        appointment.setStudentName(student.getDisplayName());
        appointment.setStudentEmail(student.getEmail());
        return appointment;
    }

    public AppointmentsModel removeAppointment(String appointmentId){
        AppointmentsModel appointment = getAppointment(appointmentId);
        if(!(appointment == null) && !appointment.getStartDate().before(new Date())){
            ApiFuture<WriteResult> writeResult = FirestoreHandler.get().collection("appointments").document(appointmentId).delete();
        }
        /* Asynchronously remove appointment from DB */
        return appointment;
    }

}