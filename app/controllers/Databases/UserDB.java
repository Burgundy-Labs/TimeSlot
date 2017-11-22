package controllers.Databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import controllers.ApplicationComponents.Roles;
import models.UsersModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
* UserDB works with UsersModel to retrieve and remove users in the Firestore DB.*/
public class UserDB {

    public static UsersModel getUser(String userId) {
        /* Return null user if none found */
        UsersModel userFound = null;
        /* Get the specific user reference from the DB*/
        DocumentReference docRef = FirestoreDB.getFirestoreDB().collection("users").document(userId);
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
            userFound = new UsersModel(
                    document.getString("display_name"),
                    document.getString("email"),
                    document.getBoolean("email_verified"),
                    document.getString("photo_url"),
                    document.getId(),
                    document.getString("phone_number"),
                    Roles.getRole(document.getString("role"))
            );
        } else {
            /* Log something */
        }
        return userFound;
    }

    public static synchronized List<UsersModel> getUsers() {
        List<UsersModel> userList = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreDB.getFirestoreDB().collection("users").get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all users - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate users and add them to a list for return */
        for (DocumentSnapshot document : documents) {
            UsersModel user = new UsersModel(
                    document.getString("display_name"),
                    document.getString("email"),
                    document.getBoolean("email_verified"),
                    document.getString("photo_url"),
                    document.getId(),
                    document.getString("phone_number"),
                    Roles.getRole(document.getString("role"))
                    );
            userList.add(user);
        }
        return userList;
    }

    public static synchronized List<UsersModel> getCoaches() {
        List<UsersModel> userList = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreDB.getFirestoreDB().collection("users").get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all users - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate users and add them to a list for return */
        for (DocumentSnapshot document : documents) {
            if(document.getString("role").equals("Coach")) {
                UsersModel user = new UsersModel(
                        document.getString("display_name"),
                        document.getString("email"),
                        document.getBoolean("email_verified"),
                        document.getString("photo_url"),
                        document.getId(),
                        document.getString("phone_number"),
                        Roles.getRole(document.getString("role"))
                );
                userList.add(user);
            }
        }
        return userList;
    }

    public static synchronized void addUser(UsersModel user) {
        /* Get DB instance */
        DocumentReference docRef = FirestoreDB.getFirestoreDB().collection("users").document(user.getUid());
        Map<String, Object> data = new HashMap<>();
        /* Create user model for DB insert */
        data.put("display_name", user.getDisplayName());
        data.put("email",user.getEmail());
        data.put("phone_number",user.getPhoneNumber());
        data.put("photo_url",user.getPhotoURL());
        data.put("role", user.getRole());
        data.put("email_verified",user.isEmailVerified());
        /* Asynchronously write user into DB */
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }

    public static boolean removeUser(String userId){
        /* Asynchronously remove user from DB */
        ApiFuture<WriteResult> writeResult = FirestoreDB.getFirestoreDB().collection("users").document(userId).delete();
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
