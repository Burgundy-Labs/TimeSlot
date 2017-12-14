package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class ReportsController extends Controller {

    public Result index() {
        return ok(views.html.reports.render());
    }
}
