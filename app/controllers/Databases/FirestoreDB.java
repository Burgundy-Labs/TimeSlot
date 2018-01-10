package controllers.Databases;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.springframework.context.annotation.Lazy;
import play.api.Play;
import play.inject.guice.GuiceApplicationLoader;

import java.io.IOException;
import java.io.InputStream;

@Singleton
public class FirestoreDB {
    private static Firestore firestoreDB = null;

    public static Firestore getFirestoreDB(){
        return firestoreDB;
    }
    /* Initializes the FirestoreDB DB for use in other areas within the project. */
    public FirestoreDB() throws IOException {
        InputStream serviceAccount = Play.current().environment().asJava().resourceAsStream("conf/credentials.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
        if (!(FirebaseApp.getApps().size() > 0)) {
            FirebaseApp.initializeApp(options);
        }
        firestoreDB = FirestoreClient.getFirestore();
    }
}
