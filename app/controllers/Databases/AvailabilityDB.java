package controllers.Databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import models.AvailabilityModel;

import java.util.*;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
* AppointmentsDB works with AppointmentsModel to retrieve and remove appointments in the Firestore DB.*/
public class AvailabilityDB {

    public static List<AvailabilityModel> getAvailabilitesForUser(String userId, Date start, Date end) {
        List<AvailabilityModel> availabilityTimes = new ArrayList<>();
        /* Return null appointment if none found */
        /* Get the specific appointment reference from the DB*/
        ApiFuture<QuerySnapshot> future = FirestoreDB.get().collection("availabilities").whereEqualTo("userId",userId).whereEqualTo("weekly", false).orderBy("startDate", Query.Direction.ASCENDING).whereGreaterThanOrEqualTo("startDate", start).get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for (DocumentSnapshot document : documents) {
            if ( document.getDate("startDate").before(end) ) {
                availabilityTimes.add(new AvailabilityModel(
                        document.getId(),
                        document.getString("userId"),
                        document.getDate("startDate"),
                        document.getDate("endDate"),
                        document.getBoolean("weekly")));
            } else {
                break;
            }
        }
        ApiFuture<QuerySnapshot> weeklyFuture = FirestoreDB.get().collection("availabilities").whereEqualTo("userId",userId).whereEqualTo("weekly", true).orderBy("startDate", Query.Direction.ASCENDING).whereLessThanOrEqualTo("startDate", end).get();
        List<QueryDocumentSnapshot> weeklyDocuments = null;
        try {
            weeklyDocuments = weeklyFuture.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for (DocumentSnapshot document : weeklyDocuments) {
            availabilityTimes.add(new AvailabilityModel(
                    document.getId(),
                    document.getString("userId"),
                    document.getDate("startDate"),
                    document.getDate("endDate"),
                    document.getBoolean("weekly")));
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