package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.UsersModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class LoginController extends Controller {
    public Result index() {
        return ok(views.html.login.render());
    }
    /*
    *  Retrieves information from currently signed in user (just signed in)
    *  Used to identify if they are new, and inputting them into the DB if so.
    */
    public Result signedIn() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        UsersModel user = Json.fromJson(json, UsersModel.class);
        /* Store UID in Session */
        session("signedInUser", user.getUid());
        /* Check if user is in DB */
        new UserDB();
        if(UserDB.getUser(user.getUid()) != null) {/* Don't upload existing users */}
        else {
            /* Add user with default role of student  */
            user.setRole("student");
            new UserDB().addUser(user);
        }
        /* Add user to DB with 'student' role (default) */
        return ok();
    }
}
