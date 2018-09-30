package databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import models.AppointmentTypeModel;
import models.ServiceModel;
import models.SettingsModel;

import java.util.*;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
* UserDB works with UsersModel to retrieve and remove users in the Firestore DB.*/
public class SettingsDB implements DBInterface<SettingsModel> {

    /* ID not used for settings get */
    public Optional<SettingsModel> get(String ID) {
        DocumentReference docRef = FirestoreHandler.get().collection("settings").document("settings");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        SettingsModel settings = null;
        assert document != null;
        if (document.exists()) {
            settings = document.toObject(SettingsModel.class);
        }
        settings = SettingsModel.replaceNull(settings);
        return Optional.of(settings);
    }
    @Override
    public boolean addOrUpdate(SettingsModel settings) {
        DocumentReference docRef = FirestoreHandler.get().collection("settings").document("settings");
        Map<String, Object> data = new HashMap<>();
        data.put("universityName", settings.getUniversityName());
        data.put("centerName", settings.getCenterName());
        data.put("semesterStart", settings.getSemesterStart());
        data.put("semesterEnd", settings.getSemesterEnd());
        data.put("startTime", settings.getStartTime());
        data.put("endTime", settings.getEndTime());
        data.put("siteAlert", settings.getSiteAlert());
        data.put("centerInformation", settings.getCenterInformation());
        data.put("maximumAppointments", settings.getMaximumAppointments());
        data.put("daysOpenWeekly", settings.getDaysOpenWeekly());
        /* Write settings to DB */
        ApiFuture<WriteResult> result = docRef.set(data);
        return result.isDone();
    }

    /*Not applicable to settings */
    @Override
    public Iterable<SettingsModel> getAll() { return null; }

    /* Not applicable to settings */
    @Override
    public Optional<SettingsModel> remove(String ID) {
        return Optional.empty();
    }

    /* Not applicable to settings */
    @Override
    public boolean removeAll() {
        return false;
    }

    public boolean removeAppointmentType(String appointmentTypeId) {
        /* Asynchronously remove user from DB */
        ApiFuture<WriteResult> writeResult = FirestoreHandler.get()
                .collection("settings")
                .document("settings")
                .collection("appointmentTypes")
                .document(appointmentTypeId).delete();
        try {
            /* Verify that action is complete */
            writeResult.get();
            return writeResult.isDone();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void addAppointmentType(AppointmentTypeModel appointmentType) {
        DocumentReference docRef;
        if (appointmentType.getAppointmentTypeId() == null) {
            docRef = FirestoreHandler.get().collection("settings").document("settings").collection("appointmentTypes").document();
        } else {
            docRef = FirestoreHandler.get().collection("settings").document("settings").collection("appointmentTypes").document(appointmentType.getAppointmentTypeId());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("appointmentType", appointmentType.getAppointmentType());
        data.put("weekly", appointmentType.getWeekly() == null ? false : appointmentType.getWeekly());
        data.put("oneTime", appointmentType.getOneTime() == null ? false : appointmentType.getOneTime());
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }

    public synchronized List<AppointmentTypeModel> getAppointmentTypes() {
        List<AppointmentTypeModel> appointmentTypesList = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("settings").document("settings").collection("appointmentTypes").get();
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
            AppointmentTypeModel appointmentType = new AppointmentTypeModel(
                    document.getId(),
                    document.getString("appointmentType"),
                    document.getBoolean("weekly"),
                    document.getBoolean("oneTime")
            );
            appointmentTypesList.add(appointmentType);
        }
        return appointmentTypesList;
    }

    public synchronized AppointmentTypeModel getAppointmentType(String appointmentTypeId) {
        AppointmentTypeModel appointmentType;
        /* Asynchronously retrieve all users */
        ApiFuture<DocumentSnapshot> query = FirestoreHandler.get().collection("settings").document("settings").collection("appointmentTypes").document(appointmentTypeId).get();
        DocumentSnapshot documentSnapshot = null;
        try {
            /* Attempt to get a list of all users - blocking */
            documentSnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert documentSnapshot != null;
        appointmentType = new AppointmentTypeModel(
                documentSnapshot.getId(),
                documentSnapshot.getString("appointmentType"),
                documentSnapshot.getBoolean("weekly"),
                documentSnapshot.getBoolean("oneTime")
        );
        return appointmentType;
    }

    public synchronized AppointmentTypeModel getAppointmentTypeByName(String appointmentTypeName) {
        AppointmentTypeModel appointmentType;
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("settings").document("settings").collection("appointmentTypes").whereEqualTo("appointmentType", appointmentTypeName).get();
        DocumentSnapshot documentSnapshot = null;
        try {
            /* Attempt to get a list of all users - blocking */
            if (query.get().getDocumentChanges().size() > 0) {
                documentSnapshot = query.get().getDocuments().get(0);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert documentSnapshot != null;
        appointmentType = new AppointmentTypeModel(
                documentSnapshot.getId(),
                documentSnapshot.getString("appointmentType"),
                documentSnapshot.getBoolean("weekly"),
                documentSnapshot.getBoolean("oneTime")
        );
        return appointmentType;
    }

    public synchronized void addService(ServiceModel service) {
        DocumentReference docRef;
        if (service.getServiceId() == null) {
            docRef = FirestoreHandler.get().collection("settings").document("settings").collection("services").document();
        } else {
            docRef = FirestoreHandler.get().collection("settings").document("settings").collection("services").document(service.getServiceId());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("service", service.getService());
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }

    public synchronized boolean removeService(String serviceID) {
        ApiFuture<WriteResult> writeResult = FirestoreHandler.get()
                .collection("settings")
                .document("settings")
                .collection("services")
                .document(serviceID).delete();
        try {
            /* Verify that action is complete */
            writeResult.get();
            return writeResult.isDone();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized List<ServiceModel> getServices() {
        List<ServiceModel> serviceModel = new ArrayList<>();
        /* Asynchronously retrieve all users */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("settings").document("settings").collection("services").get();
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
            serviceModel.add(service);
        }
        return serviceModel;
    }
}
