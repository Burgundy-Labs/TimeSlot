package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class CoachesController extends Controller {
    public Result index() {
        return ok(views.html.coaches.render());
    }
}
