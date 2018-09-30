package controllers;

import application_components.annotations.Authenticate;
import databases.AppointmentsDB;
import models.AppointmentsModel;
import play.cache.Cached;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

public class ReportsController extends BaseController {

    @Authenticate(role="Admin")
    public Result index() {
        return ok(views.html.pages.reports.render());
    }

    @Authenticate(role="Admin")
    public Result getAppointmentDate(Long reportStart, Long reportEnd) {
        Date start = new Date(reportStart);
        Date end = new Date(reportEnd);
        List<AppointmentsModel> appointments = appointmentsDB.getAppointmentsReportByDate(start, end);
        return ok(Json.toJson(appointments));
    }
}
