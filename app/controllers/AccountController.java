package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class AccountController extends Controller {
    public Result index() {
        return ok(views.html.account.render());
    }

    public static String getCurrentUser() {
        return session().get("signedInUser");
    }
}
