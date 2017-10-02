package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class LoginController extends Controller {
    public Result index() {
        return ok(views.html.login.render());
    }
}
