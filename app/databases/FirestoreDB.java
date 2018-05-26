package databases;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import controllers.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FirestoreDB  {

    public static Firestore get(){
        File serviceAccount = Application.getEnvironment().getFile("conf/credentials.json");
        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccount));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert credentials != null;
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
        if (!(FirebaseApp.getApps().size() > 0)) {
            FirebaseApp.initializeApp(options);
        }
        return FirestoreClient.getFirestore();
    }
    }

