package controllers;

import application_components.annotations.Authenticate;
import play.mvc.Controller;
import play.mvc.Result;

public class DashboardController extends Controller {

    @Authenticate
    public Result index() {
        return ok(views.html.pages.dashboard.render());
    }
}
