package controllers.Databases;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.v1beta1.FirestoreSettings;
import controllers.ApplicationComponents.Role;
import models.SettingsModel;
import models.UsersModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* DB classes contain the methods necessary to manage their corresponding models.
* UserDB works with UsersModel to retrieve and remove users in the Firestore DB.*/
public class SettingsDB {
    public static synchronized SettingsModel getSettings() {
        DocumentReference docRef = FirestoreDB.getFirestoreDB().collection("settings").document("settings");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        SettingsModel settings = null;
        assert document != null;
        if (document.exists()) {
            settings = document.toObject(SettingsModel.class);
        }
        settings = SettingsModel.replaceNull(settings);
        return settings;
    }

    public static synchronized void changeSettings(SettingsModel settings) {

        DocumentReference docRef = FirestoreDB.getFirestoreDB().collection("settings").document("settings");
        Map<String, Object> data = new HashMap<>();
        data.put("universityName", settings.getUniversityName());
        data.put("centerName", settings.getCenterName());

        /* Verify that colors are valid */
        Pattern pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
        Matcher matcher = pattern.matcher(settings.getPrimaryColor());
        if(matcher.matches()){
            data.put("primaryColor",settings.getPrimaryColor());
        } else {
            data.put("primaryColor", "#000000");
        }
        matcher = pattern.matcher(settings.getSecondaryColor());
        if(matcher.matches()){
            data.put("secondaryColor",settings.getSecondaryColor());
        } else {
            data.put("secondaryColor", "#ffffff");
        }

        /* Write settings to DB */
        ApiFuture<WriteResult> result = docRef.set(data);
        result.isDone();
    }
}
