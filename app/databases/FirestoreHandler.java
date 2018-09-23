package databases;

import application_components.Application;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/* Sets up the initial FireStore Client for DBs to utilize */
public class FirestoreHandler {
    private static boolean setup = false;
    private static FirestoreOptions firestoreOptions;
    public static Firestore get() {

        if(!setup) {
            File serviceAccount = Application.getEnvironment().getFile("conf/credentials.json");
            GoogleCredentials credentials = null;
            try {
                credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccount));
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert credentials != null;
            FirestoreOptions options = FirestoreOptions.newBuilder()
                    .setCredentials(credentials).setTimestampsInSnapshotsEnabled(true)
                    .build();
            options.getService();
            firestoreOptions = options;
            setup = true;
        }

        return firestoreOptions.getService();

    }
}

