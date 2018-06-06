package databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import models.AvailabilityModel;

import java.util.*;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
* AppointmentsDB works with AppointmentsModel to retrieve and remove appointments in the Firestore DB.*/
/* TODO remove completely in favor of discussed Appointment scheduling approach */
public class AvailabilityDB {
    private SettingsDB settingsDB = new SettingsDB();

    public List<AvailabilityModel> getAvailabilitesForUser(String userId, Date start, Date end) {
        List<AvailabilityModel> availabilityTimes = new ArrayList<>();
        /* Return null appointment if none found */
        /* Get the specific appointment reference from the DB*/
        ApiFuture<QuerySnapshot> future = FirestoreHandler.get().collection("availabilities").whereEqualTo("userId",userId).orderBy("startDate", Query.Direction.ASCENDING).get();
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
            } else if ( availability.getBoolean("weekly") && availability.getDate("startDate").before(end) && availability.getDate("startDate").after(settingsDB.get(null).orElseThrow(NullPointerException::new).getSemesterStart()) ) {
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

    public synchronized List<AvailabilityModel> getAvailabilities() {
        List<AvailabilityModel> availabilityTimes = new ArrayList<>();
        /* Return null appointment if none found */
        /* Get the specific appointment reference from the DB*/
        ApiFuture<QuerySnapshot> future = FirestoreHandler.get().collection("availabilities").get();
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

    public synchronized void addAvailability(AvailabilityModel availability) {
        /* Get DB instance */
        DocumentReference docRef;
        if(availability.getavailabilityId() == null) {
             docRef = FirestoreHandler.get().collection("availabilities").document();
        } else {
             docRef = FirestoreHandler.get().collection("availabilities").document(availability.getavailabilityId());
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

    public boolean removeAvailability(String availabilityId){
        /* Asynchronously remove appointment from DB */
        ApiFuture<WriteResult> writeResult = FirestoreHandler.get().collection("availabilities").document(availabilityId).delete();
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