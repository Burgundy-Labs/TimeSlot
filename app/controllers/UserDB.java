package controllers;

import com.google.firebase.database.*;
import models.UsersModel;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserDB {
    private static List<UsersModel> userList = new CopyOnWriteArrayList<>();

    static UsersModel getUser(String userID) {
        final UsersModel[] userFound = new UsersModel[1];
        final String uid = userID;
        // Get a reference to users
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userDB = database.getReference("users");
        // Attach an listener to read our users
        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()) {
                    if (user.getKey().equals(uid)) {
                        userFound[0] = user.getValue(UsersModel.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        return userFound[0];
    }

    public static synchronized List<UsersModel> getUsers() {
        return userList;
    }

    void addUser(UsersModel model) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");
        DatabaseReference usersRef = ref.child("users");
        usersRef.push().setValue(model);
    }
}
