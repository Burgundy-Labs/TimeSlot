package controllers;

import application_components.Authenticate;
import play.mvc.Controller;
import play.mvc.Result;

public class DashboardController extends Controller {
    private UserController userController = new UserController();

    @Authenticate()
    public Result index() {
        return ok(views.html.dashboard.render());
    }
}
