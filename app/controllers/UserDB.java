package controllers;

import com.google.firebase.database.*;
import models.UsersModel;
import play.shaded.ahc.io.netty.util.internal.ConcurrentSet;

import java.util.*;

public class UserDB {
    static Set<UsersModel> coachList = new ConcurrentSet<>();

    public synchronized Set<UsersModel> getCoachList(){
        return coachList;
    }

    synchronized void addUser(UsersModel model) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");
        DatabaseReference usersRef = ref.child("users");
        usersRef.push().setValue(model);
    }
}
