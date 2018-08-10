package databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import models.AppointmentsModel;
import models.ServiceModel;
import models.UsersModel;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/* DB classes contain the methods necessary to manage their corresponding models.
* AppointmentsDB works with AppointmentsModel to retrieve and remove appointments in the Firestore DB.*/
public class AppointmentsDB implements DBInterface<AppointmentsModel> {
    private UserDB userDB = new UserDB();


    @Override
    public Optional<AppointmentsModel> get(String ID) {
         /* Return null appointment if none found */
        AppointmentsModel appointmentFound = null;
        /* Get the specific appointment reference from the DB*/
        DocumentReference docRef = FirestoreHandler.get().collection("appointments").document(ID);
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
            appointmentFound = document.toObject(AppointmentsModel.class);
        }
        return Optional.of(appointmentFound);
    }

    @Override
    public Iterable<AppointmentsModel> getAll() {
        List<AppointmentsModel> appointmentsList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documents) {
            AppointmentsModel appointment = document.toObject(AppointmentsModel.class);
            appointmentsList.add(appointment);
        }
        return appointmentsList;
    }

    @Override
    public boolean addOrUpdate(AppointmentsModel appointment) {
        /* Get DB instance */
        DocumentReference docRef;
        if(appointment.getAppointmentId() == null) {
            docRef = FirestoreHandler.get().collection("appointments").document();
        } else {
            docRef = FirestoreHandler.get().collection("appointments").document(appointment.getAppointmentId());
        }

        /* Create user model for DB insert */

        /* Student Details */
        if (appointment.getStudentId() == null) {
            appointment.setStudentEmail(null);
            appointment.setStudentId(null);
            appointment.setStudentName(null);
            appointment.setStudentPhoto(null);
        } else {
            Optional<UsersModel> s = userDB.get(appointment.getStudentId());
            UsersModel student = s.orElseThrow(NullPointerException::new);
            appointment.setStudentName(student.getDisplayName());
            appointment.setStudentEmail(student.getEmail());
            appointment.setStudentPhoto(student.getPhotoURL());
        }

        /* Coach Details */
        Optional<UsersModel> c = userDB.get(appointment.getCoachId());
        UsersModel coach = c.orElseThrow(NullPointerException::new);
        appointment.setCoachName(coach.getDisplayName());
        appointment.setCoachEmail(coach.getEmail());
        appointment.setCoachPhoto(coach.getPhotoURL());

        appointment.setAppointmentId(docRef.getId());

        /* Asynchronously write appointment into DB */
        ApiFuture<WriteResult> result = docRef.set(appointment);
        return result.isDone();
    }

    @Override
    public Optional<AppointmentsModel> remove(String ID) {
        AppointmentsModel appointment = get(ID).orElseThrow(NullPointerException::new);
        if(appointment.getStartDate().after(new Date())){
            ApiFuture<WriteResult> writeResult = FirestoreHandler.get().collection("appointments").document(ID).delete();
        }
        /* Asynchronously remove appointment from DB */
        return Optional.of(appointment);
    }

    @Override
    public boolean removeAll() {
        try {
            ApiFuture<QuerySnapshot> appointmentFuture = FirestoreHandler.get().collection("appointments").get();
            List<QueryDocumentSnapshot> appointmentsToDelete = appointmentFuture.get().getDocuments();
            for (QueryDocumentSnapshot appointment : appointmentsToDelete) {
                remove(appointment.getId());
            }
            return true;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    /* TODO test method + add overrides for type / service / dates */
    /* Returns all available appointments */
    public List<AppointmentsModel> getAvailableAppointments() {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").whereEqualTo("studentId", null).whereGreaterThan("start_date", new Date()).get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        appointmentList = documents.stream().map(d -> d.toObject(AppointmentsModel.class)).collect(Collectors.toList());
        return appointmentList;
    }

    public List<AppointmentsModel> getByWeeklyId(String weeklyId) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").whereEqualTo("weeklyId", weeklyId).get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        appointmentList = documents.stream().map(d -> d.toObject(AppointmentsModel.class)).collect(Collectors.toList());
        return appointmentList;
    }

    /* Returns all available appointments for a coach */
    public List<AppointmentsModel> getAvailableAppointments(UsersModel coachID) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").whereEqualTo("coachId", coachID).whereEqualTo("studentId", null).whereGreaterThan("start_date", new Date()).get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        appointmentList = documents.stream().map(d -> d.toObject(AppointmentsModel.class)).collect(Collectors.toList());
        return appointmentList;
    }

    public List<AppointmentsModel> getAvailableAppointments(UsersModel coachID, Date start, Date end) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").whereEqualTo("coachId", coachID).whereEqualTo("studentId", null).whereGreaterThan("start_date", start).whereGreaterThan("start_date", end).get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        appointmentList = documents.stream().map(d -> d.toObject(AppointmentsModel.class)).collect(Collectors.toList());
        return appointmentList;
    }

    public List<AppointmentsModel> getAppointmentsForUser(String role, String userId) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> coachQuery = FirestoreHandler.get().collection("appointments").orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("coachId",userId).get();
        ApiFuture<QuerySnapshot> studentQuery = FirestoreHandler.get().collection("appointments").orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("studentId",userId).get();

        QuerySnapshot querySnapshotCoach = null;
        QuerySnapshot querySnapshotStudent = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshotCoach = coachQuery.get();
            querySnapshotStudent = studentQuery.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshotCoach != null;
        assert querySnapshotStudent != null;
        List<QueryDocumentSnapshot> documentsCoach = querySnapshotCoach.getDocuments();
        List<QueryDocumentSnapshot> documentsStudent = querySnapshotStudent.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documentsCoach) {
            AppointmentsModel appointment = document.toObject(AppointmentsModel.class);
            appointmentList.add(appointment);
        }
        for (DocumentSnapshot document : documentsStudent) {
            AppointmentsModel appointment = document.toObject(AppointmentsModel.class);
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public List<AppointmentsModel> getCoachAvailablitiliy(String userId, Date start, Date end) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();

        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").whereEqualTo("coachId", userId).whereGreaterThan("startDate", start).whereLessThan("startDate", end).get();

        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documentsCoach = querySnapshot.getDocuments();

        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documentsCoach) {
            AppointmentsModel appointment = document.toObject(AppointmentsModel.class);
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public List<AppointmentsModel> getNextFiveAppointmentsForUser(String role, String userId) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> coachQuery = FirestoreHandler.get().collection("appointments").whereGreaterThan("start_date",new Date()).orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("coachId",userId).limit(5).get();
        ApiFuture<QuerySnapshot> studentQuery = FirestoreHandler.get().collection("appointments").whereGreaterThan("start_date",new Date()).orderBy("start_date", Query.Direction.ASCENDING).whereEqualTo("studentId",userId).limit(5).get();
        QuerySnapshot querySnapshotCoach = null;
        QuerySnapshot querySnapshotStudent = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshotCoach = coachQuery.get();
            querySnapshotStudent = studentQuery.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshotCoach != null;
        assert querySnapshotStudent != null;
        List<QueryDocumentSnapshot> documentsCoach = querySnapshotCoach.getDocuments();
        List<QueryDocumentSnapshot> documentsStudent = querySnapshotStudent.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documentsCoach) {
            AppointmentsModel appointment = document.toObject(AppointmentsModel.class);
            appointmentList.add(appointment);
        }
        for (DocumentSnapshot document : documentsStudent) {
            AppointmentsModel appointment = document.toObject(AppointmentsModel.class);
            appointmentList.add(appointment);
        }
        appointmentList.sort(Comparator.comparing(AppointmentsModel::getStartDate));
        if ( appointmentList.size() > 5 ) {
            appointmentList = appointmentList.subList(0, 5);
        }
        return appointmentList;
    }

    public List<AppointmentsModel> getAppointmentsByUserAndDate(String userId, Date start, Date end) {
        Optional<UsersModel> u = userDB.get(userId);
        UsersModel user = u.orElseThrow(NullPointerException::new);
        List<AppointmentsModel> appointmentList = getAppointmentsByDate(start, end);
        if( user.getRole().equals("Coach")){
            appointmentList.removeIf(i -> !i.getCoachId().equals(user.getUid()) && !i.getStudentId().equals(user.getUid()));
        } else {
            appointmentList.removeIf(i -> !i.getStudentId().equals(user.getUid()));
        }
        return appointmentList;
    }


    public List<AppointmentsModel> getOpenAppointmentsByUserAndDate(String userId, Date start, Date end) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").whereEqualTo("coachId", userId).whereEqualTo("studentId", null).whereGreaterThanOrEqualTo("startDate", start).whereLessThan("startDate", end).get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documents) {
            AppointmentsModel appointment = document.toObject(AppointmentsModel.class);
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public List<AppointmentsModel> getAppointmentsByDate(Date start, Date end) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").orderBy("startDate", Query.Direction.ASCENDING).whereGreaterThanOrEqualTo("startDate",start).get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documents) {
            if(document.getDate("end_date").before(end)){
            AppointmentsModel appointment = document.toObject(AppointmentsModel.class);
            appointmentList.add(appointment);
            } else {
                break;
            }
        }
        return appointmentList;
    }


    public List<AppointmentsModel> getWeeklyAppointmentsByWeeklyId(String weeklyId) {
        List<AppointmentsModel> appointmentList = new ArrayList<>();
        /* Asynchronously retrieve all appointments */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("appointments").whereEqualTo("weeklyId", weeklyId).whereGreaterThan("startDate", new Date()).get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all appointments - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate appointments and add them to a list for return */
        for (DocumentSnapshot document : documents) {
                AppointmentsModel appointment = document.toObject(AppointmentsModel.class);
                appointmentList.add(appointment);
        }
        return appointmentList;
    }
}