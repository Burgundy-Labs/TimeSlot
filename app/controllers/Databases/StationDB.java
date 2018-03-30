package controllers.Databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import models.AppointmentsModel;
import models.UsersModel;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class StationDB {
    public static synchronized void addUserInCenter(UsersModel user) {
        DocumentReference docRefForLogs = FirestoreDB.get().collection("station").document("station").collection("log").document();
        Map<String, Object> log = new HashMap<>();
        log.put("userId", user.getUid());
        log.put("timeIn", new Date());
        ApiFuture<WriteResult> resultLog = docRefForLogs.set(log);
        resultLog.isDone();

        DocumentReference docRefForInStation = FirestoreDB.get().collection("station").document("station").collection("inCenter").document();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUid());
        data.put("logId", docRefForLogs.getId());
        ApiFuture<WriteResult> result = docRefForInStation.set(data);
        result.isDone();
    }

    public static synchronized void removeUserInCenter(UsersModel user) {
        ApiFuture<QuerySnapshot> query = FirestoreDB.get().collection("station").document("station").collection("inCenter").whereEqualTo("userId", user.getUid()).get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        if ( querySnapshot.getDocuments().size() > 0 ) {
            QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
            ApiFuture<WriteResult> writeResult = FirestoreDB.get().collection("station").document("station").collection("inCenter").document(document.getId()).delete();
            try {
                writeResult.get();
                DocumentReference docRefForLogs = FirestoreDB.get().collection("station").document("station").collection("log").document(document.getString("logId"));
                Map<String, Object> log = new HashMap<>();
                log.put("userId", user.getUid());
                log.put("timeIn", docRefForLogs.get().get().get("timeIn"));
                log.put("timeOut", new Date());
                ApiFuture<WriteResult> resultLog = docRefForLogs.set(log);
                resultLog.isDone();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized List<UsersModel> getUsersInCenter(){
        List<UsersModel> userModels = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreDB.get().collection("station").document("station").collection("inCenter").get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all users - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate users and add them to a list for return */
        for (DocumentSnapshot document : documents) {
            UsersModel user = UserDB.getUser(document.getString("userId"));
            userModels.add(user);
        }
        return userModels;
    }
}
