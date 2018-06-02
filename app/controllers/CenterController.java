package controllers;

import application_components.Authenticate;
import play.mvc.Controller;
import play.mvc.Result;

public class CenterController extends Controller {

    @Authenticate
    public Result index() {
        return ok(views.html.center.render());
    }
}
