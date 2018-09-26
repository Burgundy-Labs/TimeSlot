package controllers;

import application_components.annotations.Authenticate;
import play.mvc.Result;

public class ScheduleController extends BaseController {

    @Authenticate
    public Result index() {
        return ok(views.html.pages.schedule.render());
    }
}
