package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class CenterController extends Controller {

    public Result index() {
        return ok(views.html.center.render());
    }
}
