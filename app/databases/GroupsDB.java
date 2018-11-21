package databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import models.AppointmentTypeModel;
import models.GroupsModel;
import models.ServiceModel;
import models.SettingsModel;
import models.UserAttributes;
import models.UsersModel;

import java.text.SimpleDateFormat;
import java.time.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
 * UserDB works with UsersModel to retrieve and remove users in the Firestore DB.*/
public class GroupsDB implements DBInterface<GroupsModel>{

    public Optional<GroupsModel> get(String ID) {
        /* Return null group if none found */
        GroupsModel groupFound = null;
        /* Get the specific group reference from the DB*/
        DocumentReference docRef = FirestoreHandler.get().collection("groups").document(ID);
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
            groupFound = document.toObject(GroupsModel.class);
        }
        return Optional.of(groupFound);
    }

    public Optional<GroupsModel.GroupInstanceModel> getInstance(String groupID, String ID) {
        /* Return null group if none found */
        GroupsModel.GroupInstanceModel instanceFound = null;
        /* Get the specific group reference from the DB*/
        DocumentReference docRef = FirestoreHandler.get().collection("groups").document(groupID).collection("instances").document(ID);
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
            instanceFound = document.toObject(GroupsModel.GroupInstanceModel.class);
        }
        return Optional.of(instanceFound);
    }

    @Override
    public Iterable<GroupsModel> getAll() {
        List<GroupsModel> groupsList = new ArrayList<>();

        /* Asynchronously retrieve all groups */
        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("groups").get();
        QuerySnapshot querySnapshot = null;
        try {
            /* Attempt to get a list of all groups - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate groups and add them to a list for return */
        for (DocumentSnapshot document : documents) {
            GroupsModel group = document.toObject(GroupsModel.class);
            groupsList.add(group);
        }
        return groupsList;
    }

    public Iterable<GroupsModel.GroupInstanceModel> getAllInstances(String ID) {
        List<GroupsModel.GroupInstanceModel> instanceList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("groups").document(ID).collection("instances").get();
        QuerySnapshot querySnapshot = null;

        try {
            /* Attempt to get a list of all instances - blocking */
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert querySnapshot != null;
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        /* Iterate instances and add them to a list for return */
        for (DocumentSnapshot document : documents) {
            GroupsModel.GroupInstanceModel instance = document.toObject(GroupsModel.GroupInstanceModel.class);
            instanceList.add(instance);
        }
        return instanceList;
    }

    @Override
    public boolean addOrUpdate(GroupsModel group) {
        DocumentReference docRef;
        Calendar cal = Calendar.getInstance();
        cal.setTime(group.getEndTime());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        if(group.getGroupID() == null) {
            docRef = FirestoreHandler.get().collection("groups").document();
            List<Date> dates = getDateStampStart(group.getStartDate(), group.getEndDate(),
                                        group.getStartTime(), group.getRecur());
            for (Date stamp: dates) {
                cal.setTime(stamp);
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                Date endStamp = cal.getTime();
                addInstance(group, stamp, endStamp);
            }
        } else {
            docRef = FirestoreHandler.get().collection("groups").document(group.getGroupID());
        }

        group.setGroupID(docRef.getId());

        /* Asynchronously write group into DB */
        ApiFuture<WriteResult> result = docRef.set(group);
        return result.isDone();
    }

    private boolean addInstance(GroupsModel group, Date dateStamp, Date endStamp) {
        DocumentReference docRef;
        docRef = FirestoreHandler.get().collection("groups").document(group.getGroupID()).collection("instances").document();

        List<String> presentIDs = new ArrayList<>();
        GroupsModel.GroupInstanceModel instance = group.new GroupInstanceModel(null, docRef.getId(),
                presentIDs, group.getGroupID(), dateStamp, endStamp);

        ApiFuture<WriteResult> result = docRef.set(instance);
        return result.isDone();
    }

    @Override
    public Optional<GroupsModel> remove(String ID) {
        GroupsModel group = get(ID).orElseThrow(NullPointerException::new);
        if(group.getGroupID() == null){
            try {
                FirestoreHandler.get().collection("groups").document(ID).delete().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        /* Asynchronously remove group from DB */
        return Optional.of(group);
    }

    @Override
    public boolean removeAll() {
        try {
            ApiFuture<QuerySnapshot> groupFuture = FirestoreHandler.get().collection("groups").get();
            List<QueryDocumentSnapshot> groupsToDelete = groupFuture.get().getDocuments();
            for (QueryDocumentSnapshot group : groupsToDelete) {
                remove(group.getId());
            }
            return true;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    public boolean updateInstance(GroupsModel.GroupInstanceModel instance) {
        DocumentReference docRef;
        docRef = FirestoreHandler.get().collection("groups").document(instance.getGroupID()).collection("instances").document(instance.getInstanceID());
        ApiFuture<WriteResult> result = docRef.set(instance);
        return result.isDone();
    }

    private List<Date> getDateStampStart(Date start, Date end, Date startTime, List<Integer> recur) {
        List<Date> dates = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);

        cal.setTime(start);
        while (cal.getTime().before(end) || cal.getTime().equals(end)) {
            int current = cal.get(Calendar.DAY_OF_WEEK);
            for (int recurDay: recur) {
                if (current == recurDay) {
                    Calendar stamp = Calendar.getInstance();
                    stamp.setTime(cal.getTime());
                    stamp.set(Calendar.HOUR_OF_DAY, hour);
                    stamp.set(Calendar.MINUTE, minutes);

                    dates.add(stamp.getTime());
                    break;
                }
            }
            cal.add(Calendar.DATE, 1);
        }

        return dates;
    }

    public static void main(String[] args) {
        List<String> coaches = new ArrayList<>();
        coaches.add("cnPylOXnLpSANT2v943wbnfXIsU2");

        List<String> students = new ArrayList<>();
        students.add("QDig3Ue5lgdnTNpQMa3fj9ArBCF2");

        List<String> request = new ArrayList<>();

        List<Integer> recur = new ArrayList<>();
        recur.add(4);

        Date startDate = new GregorianCalendar(2018, Calendar.NOVEMBER, 25, 0, 0).getTime();
        Date endDate = new GregorianCalendar(2018, Calendar.DECEMBER, 25, 0, 0).getTime();
        Date startTime = new GregorianCalendar(0, 0, 0, 11, 20).getTime();
        Date endTime = new GregorianCalendar(0, 0, 0, 12, 20).getTime();
        GroupsModel group = new GroupsModel(coaches, students, request, null, recur,
                    "this is a group", 10, startDate, endDate, startTime, endTime, "group", "rekhi",
                true);
        GroupsDB db = new GroupsDB();
        db.addOrUpdate(group);
    }
}
