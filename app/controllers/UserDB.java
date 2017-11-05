package controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import models.UsersModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserDB {
    static UsersModel getUser(String userId) {
      UsersModel userFound = null;
        DocumentReference docRef = Firebase.getFirestoreDB().collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert document != null;
        if (document.exists()) {
            System.out.println("Document data: " + document.getData());
            userFound = new UsersModel(
                    document.getString("display_name"),
                    document.getString("email"),
                    document.getBoolean("email_verified"),
                    document.getString("photo_url"),
                    document.getId(),
                    document.getString("phone_number"),
                    document.getString("role")
            );
        } else {
            /* Log something */
        }
        return userFound;
    }

    public static synchronized List<UsersModel> getUsers() {
        List<UsersModel> userList = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = Firebase.getFirestoreDB().collection("users").get();

        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<DocumentSnapshot> documents = querySnapshot.getDocuments();

        for (DocumentSnapshot document : documents) {
            UsersModel user = new UsersModel(
                    document.getString("display_name"),
                    document.getString("email"),
                    document.getBoolean("email_verified"),
                    document.getString("photo_url"),
                    document.getId(),
                    document.getString("phone_number"),
                    document.getString("role")
                    );
            userList.add(user);
        }
        return userList;
    }

    static synchronized void addUser(UsersModel model) {
        /* Get DB instance */
        DocumentReference docRef = Firebase.getFirestoreDB().collection("users").document(model.getUid());
        Map<String, Object> data = new HashMap<>();

        /* Create user model for DB insert */
        data.put("display_name", model.getDisplayName());
        data.put("email",model.getEmail());
        data.put("phone_number",model.getPhoneNumber());
        data.put("photo_url",model.getPhotoURL());
        data.put("role", model.getRole());
        data.put("email_verified",model.isEmailVerified());
        /* Asynchronously write user into DB */
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }

    static boolean removeUser(String userId){
        ApiFuture<WriteResult> writeResult = Firebase.getFirestoreDB().collection("users").document(userId).delete();
        try {
            writeResult.get();
            return writeResult.isDone();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }
}
