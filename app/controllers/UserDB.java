package controllers;

import com.google.firebase.database.*;
import models.UsersModel;
import play.shaded.ahc.io.netty.util.internal.ConcurrentSet;

import java.util.*;

public class UserDB {
    public static Set<UsersModel> coachList = new ConcurrentSet<>();

    void addUser(UsersModel model) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");
        DatabaseReference usersRef = ref.child("users");
        usersRef.push().setValue(model);
    }
}
