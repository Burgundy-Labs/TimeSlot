package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class makeAppointmentController extends Controller{

    public Result index() {
        return ok(views.html.makeAppointment.render());
    }

}
