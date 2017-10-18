package controllers;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.inject.Inject;
import models.UsersModel;
import play.Environment;
import play.api.Play;

import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Firebase {

    public Firebase() {
        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream(Environment.simple().resource("project-burgundy-firebase-adminsdk-938fc-ccc54ba17f.json").getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert serviceAccount != null;
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(serviceAccount)
                .setDatabaseUrl("https://project-burgundy.firebaseio.com")
                .build();
            FirebaseApp.initializeApp(options);
        loadListeners();
    }

    private void loadListeners() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userDB = database.getReference("users");
        userDB.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                new UserDB().getCoachList().add(dataSnapshot.getValue(UsersModel.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                    new UserDB().getCoachList().remove(dataSnapshot.getValue(UsersModel.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
