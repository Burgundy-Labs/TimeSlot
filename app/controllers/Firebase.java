package controllers;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import models.UsersModel;
import play.api.Play;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Firebase {

    public Firebase() {
        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream(Play.current().resource("project-burgundy-firebase-adminsdk-938fc-ccc54ba17f.json").get().getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(serviceAccount)
                .setDatabaseUrl("https://project-burgundy.firebaseio.com")
                .build();
        if(FirebaseApp.getApps().size() == 0) {
            FirebaseApp.initializeApp(options);
        }
    }

}
