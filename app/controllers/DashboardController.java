package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class DashboardController extends Controller {
    public Result index() {
        String currentRole = UserController.getCurrentRole();
        /* Force redirect to Login is the user isn't signed in */
        if(currentRole == null) {
            return unauthorized(views.html.error_pages.unauthorized.render());
        }
        return ok(views.html.dashboard.render());
    }
}
