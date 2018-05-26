package controllers.Databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import models.AvailabilityModel;
import play.Logger;

import java.util.*;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
* AppointmentsDB works with AppointmentsModel to retrieve and remove appointments in the Firestore DB.*/
/* TODO remove completely in favor of discussed Appointment scheduling approach */
public class AvailabilityDB {

    public static List<AvailabilityModel> getAvailabilitesForUser(String userId, Date start, Date end) {
        List<AvailabilityModel> availabilityTimes = new ArrayList<>();
        /* Return null appointment if none found */
        /* Get the specific appointment reference from the DB*/
        ApiFuture<QuerySnapshot> future = FirestoreDB.get().collection("availabilities").whereEqualTo("userId",userId).orderBy("startDate", Query.Direction.ASCENDING).get();
        List<QueryDocumentSnapshot> availabilities = null;
        try {
            availabilities = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert availabilities != null;
        for (DocumentSnapshot availability : availabilities) {
            if ( !availability.getBoolean("weekly") && availability.getDate("startDate").before(end) && availability.getDate("endDate").after(start) ) {
                availabilityTimes.add(new AvailabilityModel(
                        availability.getId(),
                        availability.getString("userId"),
                        availability.getDate("startDate"),
                        availability.getDate("endDate"),
                        availability.getBoolean("weekly")));
            } else if ( availability.getBoolean("weekly") && availability.getDate("startDate").before(end) && availability.getDate("startDate").after(SettingsDB.getSettings().getSemesterStart()) ) {
                availabilityTimes.add(new AvailabilityModel(
                        availability.getId(),
                        availability.getString("userId"),
                        availability.getDate("startDate"),
                        availability.getDate("endDate"),
                        availability.getBoolean("weekly")));
            }
        }
        return availabilityTimes;
    }

    public static synchronized List<AvailabilityModel> getAvailabilities() {
        List<AvailabilityModel> availabilityTimes = new ArrayList<>();
        /* Return null appointment if none found */
        /* Get the specific appointment reference from the DB*/
        ApiFuture<QuerySnapshot> future = FirestoreDB.get().collection("availabilities").get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for (DocumentSnapshot document : documents) {
            availabilityTimes.add(new AvailabilityModel(
                    document.getId(),
                    document.getString("userId"),
                    document.getDate("startDate"),
                    document.getDate("endDate"),
                    document.getBoolean("weekly")));
        }
        return availabilityTimes;
    }

    public static synchronized void addAvailability(AvailabilityModel availability) {
        /* Get DB instance */
        DocumentReference docRef;
        if(availability.getavailabilityId() == null) {
             docRef = FirestoreDB.get().collection("availabilities").document();
        } else {
             docRef = FirestoreDB.get().collection("availabilities").document(availability.getavailabilityId());
        }
        Map<String, Object> data = new HashMap<>();
        /* Create availability model for DB insert */
        data.put("appointmentId", docRef.getId());
        data.put("userId", availability.getUserid());
        data.put("startDate", availability.getStartDate());
        data.put("endDate", availability.getEndDate());
        data.put("weekly",availability.getWeekly());
        /* Asynchronously write appointment into DB */
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }

    public static boolean removeAvailability(String availabilityId){
        /* Asynchronously remove appointment from DB */
        ApiFuture<WriteResult> writeResult = FirestoreDB.get().collection("availabilities").document(availabilityId).delete();
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