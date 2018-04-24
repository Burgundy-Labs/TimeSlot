package controllers;

import controllers.Databases.AppointmentsDB;
import controllers.Databases.SettingsDB;
import models.AppointmentTypeModel;
import models.AppointmentsModel;
import models.ServiceModel;
import models.UsersModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.*;
import java.util.stream.Collectors;

public class ReportsController extends Controller {

    public Result index() {
        Application.restrictByRole("Admin");
        return ok(views.html.reports.render());
    }

    public Result getAppointmentDate(Long reportStart, Long reportEnd) {
        Application.restrictByRole("Admin");
        Date start = new Date(reportStart);
        Date end = new Date(reportEnd);
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsByDate(start, end);
        return ok(Json.toJson(appointments));
    }
}
