package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class DashboardController extends Controller {

    public Result index() {
        return ok(views.html.dashboard.render());
    }

}
