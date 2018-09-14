package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import databases.UserDB;
import models.UsersModel;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Optional;

public class LoginController extends BaseController {
    private UserDB userDB = new UserDB();

    public Result index() {
        return ok(views.html.pages.login.render());
    }
    /*
    *  Retrieves information from currently signed in user (just signed in)
    *  Used to identify if they are new, and inputting them into the DB if so.
    */
    public Result signedIn() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Check if user is in DB */
        Optional<UsersModel> u = userDB.get(json.get("uid").asText());
        UsersModel user = new UsersModel();
        if(!u.isPresent()) {
            /* If user was not found - create a new one and add them */
            user.setRole("Student");
            user.setDisplayName(json.get("displayName").asText());
            user.setPhotoURL(json.get("photoURL").asText());
            user.setUid(json.get("uid").asText());
            user.setEmail(json.get("email").asText());
            user.setSubscribed(true);
            userDB.addOrUpdate(user);
        } else {
            user = u.get();
            userDB.addOrUpdate(user);
        }

        /* Store UID in Session */
        session("currentUser", user.getUid());
        session("currentRole", user.getRole());
        return ok();
    }

    public Result logout(){
        session().clear();
        return redirect("/");
    }
}
