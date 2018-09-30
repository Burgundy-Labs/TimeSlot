package controllers;

import application_components.annotations.Authenticate;
import play.cache.Cached;
import play.mvc.Controller;
import play.mvc.Result;

public class DashboardController extends BaseController {

    @Authenticate
    public Result index() {
        return ok(views.html.pages.dashboard.render());
    }
}
