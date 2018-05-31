package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class CenterController extends Controller {
    private UserController userController = new UserController();
    public Result index() {
        String currentRole = userController.getCurrentRole();
        /* Force redirect to Login is the user isn't signed in */
        if(currentRole == null) {
            return ok(views.html.login.render());
        }
        return ok(views.html.center.render());
    }
}
