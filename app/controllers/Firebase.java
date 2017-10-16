package controllers;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import models.UsersModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Firebase {

    public Firebase() {
        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream("../conf/project-burgundy-firebase-adminsdk-938fc-ccc54ba17f.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(serviceAccount)
                .setDatabaseUrl("https://project-burgundy.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    }

}
