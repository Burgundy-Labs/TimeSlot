package controllers.Databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import models.AppointmentsModel;
import models.UsersModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        if(role.equals("Admin")){role = "Coach";}
        ApiFuture<QuerySnapshot> query = FirestoreDB.getFirestoreDB().collection("appointments").orderBy("start_date", Query.Direction.DESCENDING).whereEqualTo(role.toLowerCase()+"Id",userId).limit(5).get();
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
                    Boolean.valueOf(document.getBoolean("present")),
                    document.getString("appointment_type"),
                    document.getString("service_type"));
            appointmentList.add(appointment);
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
                    Boolean.valueOf(document.getBoolean("present")),
                    document.getString("appointment_type"),
                    document.getString("service_type"));
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public static synchronized void addAppointment(AppointmentsModel appointment) {
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
        result.isDone();
    }

    public static boolean removeAppointment(String appointmentId){
        /* Asynchronously remove appointment from DB */
        ApiFuture<WriteResult> writeResult = FirestoreDB.getFirestoreDB().collection("appointments").document(appointmentId).delete();
        try {
            /* Verify that action is complete */
            writeResult.get();
            return writeResult.isDone();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }
}