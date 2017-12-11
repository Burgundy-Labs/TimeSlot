package controllers.Databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import controllers.MailerService;
import models.AppointmentsModel;
import models.UsersModel;

import java.util.*;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
* AppointmentsDB works with AppointmentsModel to retrieve and remove appointments in the Firestore DB.*/
public class AppointmentsDB {

    public static AppointmentsModel getAppointment(String appointmentId) {
        /* Return null appointment if none found */
        AppointmentsModel appointmentFound = null;
        /* Get the specific appointment reference from the DB*/
        DocumentReference docRef = FirestoreDB.getFirestoreDB().collection("appointments").document(appointmentId);
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
                    Boolean.valueOf(document.getBoolean("present")),
                    document.getString("appointment_type"),
                    document.getString("service_type"));
        } else {
            /* Log something */
        }
        return appointmentFound;
    }

    public static synchronized List<AppointmentsModel> getAppointmentsForUser(String role, String userId) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> coachQuery = FirestoreDB.getFirestoreDB().collection("appointments").orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("coachId",userId).get();
        ApiFuture<QuerySnapshot> studentQuery = FirestoreDB.getFirestoreDB().collection("appointments").orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("studentId",userId).get();

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
        List<DocumentSnapshot> documentsCoach = querySnapshotCoach.getDocuments();
        List<DocumentSnapshot> documentsStudent = querySnapshotStudent.getDocuments();
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
                    document.getString("service_type"));
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
                    document.getString("service_type"));
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public static synchronized List<AppointmentsModel> getNextFiveAppointmentsForUser(String role, String userId) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> coachQuery = FirestoreDB.getFirestoreDB().collection("appointments").whereGreaterThan("start_date",new Date()).orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("coachId",userId).limit(5).get();
        ApiFuture<QuerySnapshot> studentQuery = FirestoreDB.getFirestoreDB().collection("appointments").whereGreaterThan("start_date",new Date()).orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("studentId",userId).limit(5).get();

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
        List<DocumentSnapshot> documentsCoach = querySnapshotCoach.getDocuments();
        List<DocumentSnapshot> documentsStudent = querySnapshotStudent.getDocuments();
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
                    document.getString("service_type"));
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
                    document.getString("service_type"));
            appointmentList.add(appointment);
        }
        Collections.sort(appointmentList, new Comparator<AppointmentsModel>() {
            public int compare(AppointmentsModel app1, AppointmentsModel app2) {
                return app1.getStartDate().compareTo(app2.getStartDate());
            }
        });
        if ( appointmentList.size() > 5 ) {
            appointmentList = appointmentList.subList(0, 5);
        }
        return appointmentList;
    }

    public static synchronized List<AppointmentsModel> getAppointments() {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreDB.getFirestoreDB().collection("appointments").get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
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
                    document.getString("service_type"));
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public static synchronized AppointmentsModel addAppointment(AppointmentsModel appointment) {
        /* Get DB instance */
        DocumentReference docRef;
        if(appointment.getAppointmentId() == null) {
            docRef = FirestoreDB.getFirestoreDB().collection("appointments").document();
        } else {
            docRef = FirestoreDB.getFirestoreDB().collection("appointments").document(appointment.getAppointmentId());
        }
        Map<String, Object> data = new HashMap<>();
        /* Create user model for DB insert */
        UsersModel student = UserDB.getUser(appointment.getStudentId());
        UsersModel coach = UserDB.getUser(appointment.getCoachId());
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

        /* Asynchronously write appointment into DB */
        ApiFuture<WriteResult> result = docRef.set(data);

        /* Add required email information to appointment model for emails */
        appointment.setCoachName(coach.getDisplayName());
        appointment.setCoachEmail(coach.getEmail());
        appointment.setStudentName(student.getDisplayName());
        appointment.setStudentEmail(student.getEmail());
        return appointment;
    }

    public static AppointmentsModel removeAppointment(String appointmentId){
        AppointmentsModel appointment = getAppointment(appointmentId);
        /* Asynchronously remove appointment from DB */
        ApiFuture<WriteResult> writeResult = FirestoreDB.getFirestoreDB().collection("appointments").document(appointmentId).delete();
        return appointment;
    }
}