package controllers;

import models.UsersModel;
import play.mvc.Controller;
import play.mvc.Result;

public class DashboardController extends Controller {
    public Result index() {
        UsersModel u = UserController.getCurrentUser();
        if(u == null || u.getUid() == null) {
            return ok(views.html.login.render());
        }
        return ok(views.html.dashboard.render());
    }
}
