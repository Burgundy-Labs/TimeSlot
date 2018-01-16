package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationComponents.Roles;
import controllers.Databases.UserDB;
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
        /* Check if user is in DB */
        UsersModel u = UserDB.getUser(user.getUid());
        if (u == null) {
            user.setRole(Roles.getRole("Student"));
            UserDB.addUser(user);
            session("newUser", "true");
        } else {
            user = u;
        }
        UserDB.addUser(user);
        /* Store UID in Session */
        session("currentUser", user.getUid());
        /* Add user to DB with 'student' role (default) */
        return ok();
    }

    public Result logout(){
        session().clear();
        return redirect("/Login");
    }
}
