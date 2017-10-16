package controllers;

import com.google.firebase.database.*;
import models.UsersModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDB {
    private static Set<UsersModel> userList = new HashSet<>();

    public Set<UsersModel> getUserList() {
        /* Listeners */
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    userList.add(data.getValue(UsersModel.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return userList;
    }
}
