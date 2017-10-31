package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

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
        JsonNode user = request().body().asJson();

        /* Store UID in Session */
        session("signedInUser", user.get("uid").asText());

        /* Check if user is in DB */

        /* Add user to DB with 'student' role (default) */
        return ok();
    }
}
