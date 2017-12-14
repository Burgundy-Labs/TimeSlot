package controllers;

import controllers.Databases.AppointmentsDB;
import models.AppointmentsModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class ReportsController extends Controller {

    public Result index() {
        return ok(views.html.reports.render());
    }

    public Result appointmentTypeStatistics() {
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointments();



        return ok(Json.toJson(appointments));
    }
}
