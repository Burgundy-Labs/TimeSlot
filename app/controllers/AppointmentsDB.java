package controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import controllers.ApplicationComponents.Role;
import models.AppointmentsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
* AppointmentsDB works with AppointmentsModel to retrieve and remove appointments in the Firestore DB.*/
public class AppointmentsDB {

    public static AppointmentsModel getUser(String userId) {
        /* Return null appointment if none found */
        AppointmentsModel userFound = null;
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
            System.out.println("Document data: " + document.getData());
            appointmentFound = new AppointmentsModel(
                    document.getString("appointment_name"),
                    document.getId(),
                    document.getDate("start_date"),
                    document.getDate("end_date"),
                    document.getString("student_name"),
                    document.getString("coach_name"),
                    document.getString("appoinment_notes"),
                    document.getBoolean("present"),
                    document.getString("appointment_type"))
            );
        } else {
            /* Log something */
        }
        return appointmentFound;
    }

    public static synchronized List<AppointmentsModel> getAppointments() {
        List<AppointmentsModel> AppointmentList = new ArrayList<>();
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
                    document.getString("appointment_name"),
                    document.getId(),
                    document.getDate("start_date"),
                    document.getDate("end_date"),
                    document.getString("student_name"),
                    document.getString("coach_name"),
                    document.getString("appoinment_notes"),
                    document.getBoolean("present"),
                    AppointmentType.getAppointmentType(document.getString("appointment_type"));
            );
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    static synchronized void addAppointment(AppointmentsModel appointment) {
        /* Get DB instance */
        DocumentReference docRef = FirestoreDB.getFirestoreDB().collection("appointments").document(appointment.getAppointmentId());
        Map<String, Object> data = new HashMap<>();
        /* Create user model for DB insert */
        data.put("appointment_name", appointment.getAppointmentName());
        data.put("start_date", appointment.getStartDate());
        data.put("end_date", appointment.getEndDate());
        data.put("student_name", appointment.getStudentName());
        data.put("coach_name", appointment.getCoachName());
        data.put("appointment_notes", appointment.getAppointmentNotes());
        data.put("present", appointment.getPresent());
        data.put("appointment_type", appointment.getAppointmentType());
        /* Asynchronously write appointment into DB */
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }

    static boolean removeAppointment(String AppointmentId){
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