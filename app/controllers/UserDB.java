package controllers;

import com.google.firebase.database.*;
import models.UsersModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDB {
    private static Set<UsersModel> coachList = new HashSet<>();

    public Set<UsersModel> getCoachList() {
        /* Listeners */
        Firebase fb = new Firebase();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    coachList.add(data.getValue(UsersModel.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return coachList;
    }
}
