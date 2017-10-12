package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class LoginController extends Controller {
    public Result index() {
        return ok(views.html.login.render());
    }

    public Result Login(String userId) {
        session("connected", userId);
        return ok(views.html.index.render("Dashboard"));
    }

    public static void LogOut(){
        session().remove("userId");
    }
}
