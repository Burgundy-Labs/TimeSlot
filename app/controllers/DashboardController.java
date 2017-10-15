package controllers;

import models.AppointmentsModel;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardController extends Controller {

    public Result index() {
        return ok(views.html.dashboard.render());
    }

}
