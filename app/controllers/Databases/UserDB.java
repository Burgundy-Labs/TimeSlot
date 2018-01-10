package controllers.Databases;

import com.google.api.Service;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import controllers.ApplicationComponents.Roles;
import models.NotificationModel;
import models.ServiceModel;
import models.UsersModel;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
* UserDB works with UsersModel to retrieve and remove users in the Firestore DB.*/
public class UserDB {

    public static synchronized void addServiceToUser(String userId, ServiceModel service){
         /* Get DB instance */
        DocumentReference docRef = FirestoreDB.getFirestoreDB().collection("users").document(userId).collection("services").document(service.getServiceId());
        Map<String, Object> data = new HashMap<>();
        /* Create user model for DB insert */
        data.put("service", service.getService());
        /* Asynchronously write user into DB */
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }

    public static synchronized List<ServiceModel> getServicesForUser(String userId){
        List<ServiceModel> servicesList = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreDB.getFirestoreDB().collection("users").document(userId).collection("services").get();
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
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime thirtyDaysAgo = now.plusDays(-30);
        for (DocumentSnapshot document : documents) {
                   ServiceModel service = new ServiceModel(
                    document.getId(),
                    document.getString("service")
            );
            servicesList.add(service);
        }
        return servicesList;
    }

    public static boolean removeServiceFromUser(String userId, String serviceId) {
        /* Asynchronously remove user from DB */
        ApiFuture<WriteResult> writeResult = FirestoreDB.getFirestoreDB().collection("users").document(userId).collection("services").document(serviceId).delete();
        try {
            /* Verify that action is complete */
            writeResult.get();
            return writeResult.isDone();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static synchronized void addNotificationToUser(NotificationModel notificationModel, String userId){
         /* Get DB instance */
        DocumentReference docRef = null;
         if(notificationModel.getNotificationId() == null) {
             docRef = FirestoreDB.getFirestoreDB().collection("users").document(userId).collection("notifications").document();
         } else {
             docRef = FirestoreDB.getFirestoreDB().collection("users").document(userId).collection("notifications").document(notificationModel.getNotificationId());
         }
        Map<String, Object> data = new HashMap<>();
        /* Create user model for DB insert */
        data.put("notificationId", docRef.getId());
        data.put("notificationContent", notificationModel.getNotificationContent());
        data.put("creationDate", new Date());
        /* Asynchronously write user into DB */
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }

    public static synchronized List<NotificationModel> getNotificaitonsForUser(String userId){
        List<NotificationModel> notificationList = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreDB.getFirestoreDB().collection("users").document(userId).collection("notifications").orderBy("creationDate", Query.Direction.DESCENDING).limit(5).get();
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
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime thirtyDaysAgo = now.plusDays(-30);
        for (DocumentSnapshot document : documents) {
            if (document.getDate("creationDate").toInstant().isBefore(thirtyDaysAgo.toInstant())) {
                removeNotification(userId, document.getString("notificationId"));
            }
            NotificationModel notification = new NotificationModel(
                    document.getString("notificationId"),
                    document.getString("notificationContent")
            );
            notificationList.add(notification);
        }
        return notificationList;
    }

    public static boolean removeNotification(String userId, String notificationId) {
        /* Asynchronously remove user from DB */
        ApiFuture<WriteResult> writeResult = FirestoreDB.getFirestoreDB().collection("users").document(userId).collection("notifications").document(notificationId).delete();
        try {
            /* Verify that action is complete */
            writeResult.get();
            return writeResult.isDone();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static UsersModel getUser(String userId) {
        /* Return null user if none found */
        UsersModel userFound = null;
        /* Get the specific user reference from the DB*/
        DocumentReference docRef = FirestoreDB.getFirestoreDB().collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        try {
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
                    Roles.getRole(document.getString("role")),
                    document.getBoolean("isCoach")
            );
        } else {

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
                    Roles.getRole(document.getString("role")),
                    document.getBoolean("isCoach")
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
            if (document.getString("role").equals("Coach")
                    || (document.getString("role").equals("Admin")
                    && (document.getBoolean("isCoach") != null)
                    && document.getBoolean("isCoach")))
            {
                UsersModel user = new UsersModel(
                        document.getString("display_name"),
                        document.getString("email"),
                        document.getBoolean("email_verified"),
                        document.getString("photo_url"),
                        document.getId(),
                        document.getString("phone_number"),
                        Roles.getRole(document.getString("role")),
                        document.getBoolean("isCoach")
                );
                userList.add(user);
            }
        }
        return userList;
    }

    public static List<UsersModel> getCoachesByService(String serviceId) {
        List<UsersModel> coachesWithService = new ArrayList<>();
        List<UsersModel> coaches = getCoaches();
        for(UsersModel c : coaches) {
            List<ServiceModel> services = getServicesForUser(c.getUid());
            for(ServiceModel s : services) {
                if (s.getServiceId().equals(serviceId)) {
                    coachesWithService.add(c);
                }
            }
        }
        return coachesWithService;
    }

    public static synchronized void addUser(UsersModel user) {
        /* Set only user present to admin */
        List<UsersModel> users = getUsers();
        if(users.size() == 0) {
            user.setRole(Roles.getRole("Admin"));
        }
        /* Get DB instance */
        DocumentReference docRef = FirestoreDB.getFirestoreDB().collection("users").document(user.getUid());
        Map<String, Object> data = new HashMap<>();
        /* Create user model for DB insert */
        data.put("display_name", user.getDisplayName());
        data.put("email", user.getEmail());
        data.put("phone_number", user.getPhoneNumber());
        data.put("photo_url", user.getPhotoURL());
        data.put("role", user.getRole());
        data.put("email_verified", user.isEmailVerified());
        if (user.getRole().equals("Admin")) {
            if (user.isCoach() == null) {
                user.setIsCoach(false);
            }
            data.put("isCoach", user.isCoach());
        }
        /* Asynchronously write user into DB */
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }

    public static boolean removeUser(String userId) {
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
