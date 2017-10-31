package controllers;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import models.UsersModel;
import play.Environment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Firebase {

    private static boolean hasBeenInitialized = false;
    public Firebase() {
        if(!hasBeenInitialized) {
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
            hasBeenInitialized = true;
            addListeners();
        }
    }

    public void addListeners() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userDB = database.getReference("users");
        userDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserDB.getUsers().add(dataSnapshot.getValue(UsersModel.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserDB.getUsers().remove(dataSnapshot.getValue(UsersModel.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                UserDB.getUsers().remove(dataSnapshot.getValue(UsersModel.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
