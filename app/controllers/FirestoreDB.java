package controllers;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import javax.inject.Singleton;

@Singleton
public class FirestoreDB {
    private static Firestore firestoreDB = null;

    public static Firestore getFirestoreDB(){
        return firestoreDB;
    }
    /* Initializes the FirestoreDB DB for use in other areas within the project. */
    public static void initialize() {
        FirestoreOptions firestoreOptions =
                FirestoreOptions.getDefaultInstance().toBuilder()
                        .setProjectId("project-burgundy")
                        .build();
        firestoreDB = firestoreOptions.getService();
    }
}
