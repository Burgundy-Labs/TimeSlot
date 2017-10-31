package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class SettingsController extends Controller {

    public Result index() {
        return ok(views.html.settings.render());
    }

}
