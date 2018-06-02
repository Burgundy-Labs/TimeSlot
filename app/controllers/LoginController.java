package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import databases.UserDB;
import models.UsersModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class LoginController extends Controller {
    private UserDB userDB = new UserDB();

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
        /* Check if user is in DB */
        UsersModel u = userDB.get(json.get("uid").asText());
        if (u == null) {
            /* If user was not found - create a new one and add them */
            u = new UsersModel();
            u.setRole("Student");
            u.setDisplayName(json.get("displayName").asText());
            u.setUid(json.get("uid").asText());
            u.setEmail(json.get("email").asText());
            u.setSubscribed(true);
            userDB.addOrUpdate(u);
        } else {
            if((u.getAuth_id() == null || u.getAuth_id().equals("")) && (json.findPath("auth_id").textValue() != null && !json.findPath("auth_id").textValue().equals(""))) {
                u.setAuth_id(json.findPath("auth_id").asText().replaceAll("\r",""));
            }
        }
        userDB.addOrUpdate(u);
        /* Store UID in Session */
        session("currentUser", u.getUid());
        session("currentRole", u.getRole());
        /* Add user to DB with 'student' role (default) */
        if(json.findPath("auth_id") != null && !json.findPath("auth_id").asText().equals("")){
            return ok().withCookies(Http.Cookie.builder("auth_id", json.findPath("auth_id").textValue().replace("\r","")).build());
        }
        return ok();
    }

    public Result logout(){
        session().clear();
        return redirect("/");
    }
}
