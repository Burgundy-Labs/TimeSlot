package databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import models.ServiceModel;
import models.UsersModel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
 * UserDB works with UsersModel to retrieve and remove users in the Firestore DB.*/
public class UserDB implements DBInterface<UsersModel> {

    @Override
    public UsersModel get(String ID) {
        /* Return null user if none found */
        UsersModel userFound = null;
        /* Get the specific user reference from the DB*/
        DocumentReference docRef = FirestoreHandler.get().collection("users").document(ID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert document != null;

        if (document.exists()) {
            userFound = document.toObject(UsersModel.class);
        }
        return userFound;
    }

    @Override
    public Iterable<UsersModel> getAll() {
        List<UsersModel> userList = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("users").get();
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
            UsersModel user = document.toObject(UsersModel.class);
            userList.add(user);
        }
        return userList;
    }

    @Override
    public boolean addOrUpdate(UsersModel user) {
        /* Set first user to admin */
        List<UsersModel> users = (List<UsersModel>) getAll();
        if (users.size() == 0) {
            user.setRole("Admin");
        }
        /* Get DB instance */
        DocumentReference docRef = FirestoreHandler.get().collection("users").document(user.getUid());
        /* Asynchronously write user into DB */
        ApiFuture<WriteResult> result = docRef.set(user);
        return result.isDone();
    }

    @Override
    public UsersModel remove(String ID) {
     /* Asynchronously remove user from DB */
        UsersModel userToDelete = get(ID);
        ApiFuture<WriteResult> writeResult = FirestoreHandler.get().collection("users").document(ID).delete();
        try {
            /* Verify that action is complete */
            writeResult.get();
            return userToDelete;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UsersModel removeAll() {
        throw new NotImplementedException();
    }

    @Override
    public File export() {
        throw new NotImplementedException();
    }

    public void addServiceToUser(String ID, ServiceModel service) {
        /* Get DB instance */
        DocumentReference docRef = FirestoreHandler.get().collection("users").document(ID).collection("services").document(service.getServiceId());
        Map<String, Object> data = new HashMap<>();
        /* Create user model for DB insert */
        data.put("service", service.getService());
        /* Asynchronously write user into DB */
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }

    public List<ServiceModel> getServicesForUser(String ID) {
        List<ServiceModel> servicesList = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("users").document(ID).collection("services").get();
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
            ServiceModel service = new ServiceModel(
                    document.getId(),
                    document.getString("service")
            );
            servicesList.add(service);
        }
        return servicesList;
    }

    public boolean removeServiceFromUser(String ID, String serviceId) {
        /* Asynchronously remove user from DB */
        ApiFuture<WriteResult> writeResult = FirestoreHandler.get().collection("users").document(ID).collection("services").document(serviceId).delete();
        try {
            /* Verify that action is complete */
            writeResult.get();
            return writeResult.isDone();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UsersModel getByAuth_ID(String ID) {
        /* Return null user if none found */
        UsersModel userFound = null;
        /* Get the specific user reference from the DB*/
        ApiFuture<QuerySnapshot> docRef = FirestoreHandler.get().collection("users").whereEqualTo("auth_id", ID).get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = docRef.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert documents != null;
        userFound = documents.get(0).toObject(UsersModel.class);
        return userFound;
    }

    public List<UsersModel> getAllByRole(String role) {
        List<UsersModel> userList = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("users").get();
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
            if (document.getString("role").equals(role)) {
                userList.add(document.toObject(UsersModel.class));
            }
        }
        return userList;
    }

    public List<UsersModel> getCoachesByService(String serviceId) {
        List<UsersModel> coachesWithService = new ArrayList<>();
        List<UsersModel> coaches = getAllByRole("Coach");
        for (UsersModel c : coaches) {
            List<ServiceModel> services = getServicesForUser(c.getUid());
            for (ServiceModel s : services) {
                if (s.getServiceId().equals(serviceId)) {
                    coachesWithService.add(c);
                    break;
                }
            }
        }
        return coachesWithService;
    }
}
