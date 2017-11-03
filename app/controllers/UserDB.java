package controllers;

import com.google.firebase.database.*;
import models.UsersModel;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserDB {
    private static List<UsersModel> userList = new CopyOnWriteArrayList<>();

    static UsersModel getUser(String userId) {
//        final UsersModel[] userFound = new UsersModel[1];
//        final String uid = userId;
//        // Get a reference to users
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference userDB = database.getReference("users");
//        // Attach an listener to read our users
//        userDB.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot user : snapshot.getChildren()) {
//                    UsersModel thisUser = user.getValue(UsersModel.class);
//                    if (thisUser.getUid().equals(uid)) {
//                        userFound[0] = thisUser;
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getMessage());
//            }
//        });
        UsersModel userFound = null;
        for(UsersModel u : userList){
            if(u.getUid().equals(userId)) userFound = u;
        }
        return userFound;
    }

    public static synchronized List<UsersModel> getUsers() {
        return userList;
    }

    static synchronized void addUser(UsersModel model) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");
        DatabaseReference usersRef = ref.child("users");
        usersRef.push().setValue(model);
    }

    static void removeUser(String userId){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");
        DatabaseReference usersRef = ref.child("users");
        usersRef.orderByChild("uid").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
