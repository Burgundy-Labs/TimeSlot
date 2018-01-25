package controllers;

import models.UsersModel;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class DashboardController extends Controller {
    public Result index() {
        String currentRole = UserController.getCurrentRole();
        /* Force redirect to Login is the user isn't signed in */
        if(currentRole == null) {
            return ok(views.html.login.render());
        }
        if(session("newUser") != null && session("newUser").equals("true")){
            session().remove("newUser");
            return ok(views.html.dashboard.render()).withCookies(Http.Cookie.builder("newUser", "true").build());
        }
        return ok(views.html.dashboard.render());
    }
}
