package databases;

import com.google.api.Service;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import controllers.SettingsController;
import controllers.UserController;
import models.ServiceModel;
import models.UserAttributes;
import models.UsersModel;

import java.util.*;
import java.util.concurrent.ExecutionException;

/* DB classes contain the methods necessary to manage their corresponding models.
 * UserDB works with UsersModel to retrieve and remove users in the Firestore DB.*/
public class UserDB implements DBInterface<UsersModel> {

	@Override
	public Optional<UsersModel> get(String ID) {
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
		return Optional.ofNullable(userFound);
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
		try {
			result.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return result.isDone();
	}

	@Override
	public Optional<UsersModel> remove(String ID) {
	 /* Asynchronously remove user from DB */
		Optional<UsersModel> userToDelete = get(ID);
		ApiFuture<WriteResult> writeResult = FirestoreHandler.get().collection("users").document(ID).delete();
		try {
			/* Verify that action is complete */
			writeResult.get();
			return Optional.of(userToDelete.orElseThrow(NullPointerException::new));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public boolean removeAll() {
		try {
			ApiFuture<QuerySnapshot> userFuture = FirestoreHandler.get().collection("users").get();
			List<QueryDocumentSnapshot> usersToDelete = userFuture.get().getDocuments();
			for (QueryDocumentSnapshot user : usersToDelete) {
			    remove(user.getId());
			}
			return true;
		} catch (InterruptedException | ExecutionException e) {
			return false;
		}
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

		DocumentReference coachRef = FirestoreHandler.get().collection("settings").document("settings").collection("services").document(service.getServiceId()).collection("coaches").document(ID);
		Map<String, Object> coachData = new HashMap<>();
		ApiFuture<WriteResult> coachResult = coachRef.set(coachData);
		coachResult.isDone();
	}

	public boolean hasService(String userId, String serviceId) {
		/* Asynchronously retrieve all users */
		DocumentReference docRef = FirestoreHandler.get().collection("users").document(userId).collection("services").document(serviceId);
		ApiFuture<DocumentSnapshot> future = docRef.get();
		DocumentSnapshot document = null;
		try {
			document = future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assert document != null;
		return document.exists();
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
					document.getString("service"),
					document.getString("prompt")
			);
			servicesList.add(service);
		}
		return servicesList;
	}

	public boolean removeServiceFromUser(String ID, String serviceId) {
		/* Asynchronously remove user from DB */
		ApiFuture<WriteResult> writeResult = FirestoreHandler.get().collection("users").document(ID).collection("services").document(serviceId).delete();
		ApiFuture<WriteResult> coachResult = FirestoreHandler.get().collection("settings").document("settings").collection("services").document(serviceId).collection("coaches").document(ID).delete();
		return writeResult.isDone() && coachResult.isDone();
	}

	public UsersModel getByAuth_ID(String ID) {
		/* Return null user if none found */
		UsersModel userFound;
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
		if (role == null) { return null; }
		ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("users").whereEqualTo("role", role).get();
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
			userList.add(document.toObject(UsersModel.class));
		}
		if (role.equals("Coach")) {
			ApiFuture<QuerySnapshot> adminQuery = FirestoreHandler.get().collection("users").whereEqualTo("role", "Admin").get();
			QuerySnapshot adminQuerySnapshot = null;
			try {
				adminQuerySnapshot = adminQuery.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			assert adminQuerySnapshot != null;
			List<QueryDocumentSnapshot> adminDocuments = adminQuerySnapshot.getDocuments();
			for (DocumentSnapshot adminDocument : adminDocuments) {
				UsersModel admin = adminDocument.toObject(UsersModel.class);
				if (UserController.hasAttribute(admin, UserAttributes.IS_COACH.getValue())) {
					userList.add(admin);
				}
			}
		}
		return userList;
	}

	public List<UsersModel> getCoachesByService(String serviceId) {
//		If DB needs to be updated with new service setup:
//		updateCoachesWithServices();
		List<UsersModel> coachList = new ArrayList<>();
		/* Asynchronously retrieve all users */
		ApiFuture<QuerySnapshot> query = FirestoreHandler.get().collection("settings").document("settings").collection("services").document(serviceId).collection("coaches").get();
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
			boolean coachExists = get(document.getId()).isPresent();
			if(coachExists){
                UsersModel coach = get(document.getId()).get();
                if ("Coach".equals(coach.getRole()) || ("Admin".equals(coach.getRole()) && UserController.hasAttribute(coach, UserAttributes.IS_COACH.getValue()))) {
                    coachList.add(coach);
                }
            }
		}
		return coachList;
	}

	public void updateCoachesWithServices() {
		List<UsersModel> coaches = getAllByRole("Coach");
		for (UsersModel c : coaches) {
			List<ServiceModel> services = getServicesForUser(c.getUid());
			for (ServiceModel s : services) {
				DocumentReference coachRef = FirestoreHandler.get().collection("settings").document("settings").collection("services").document(s.getServiceId()).collection("coaches").document(c.getUid());
				Map<String, Object> coachData = new HashMap<>();
				ApiFuture<WriteResult> coachResult = coachRef.set(coachData);
				coachResult.isDone();
			}
		}
	}
}
