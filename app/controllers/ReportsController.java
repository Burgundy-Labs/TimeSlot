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
        String currentRole = UserController.getCurrentRole();
        if (currentRole == null || !currentRole.equals("Admin")) {
            return forbidden(views.html.error_pages.unauthorized.render());
        } else {
            return ok(views.html.reports.render());
        }
    }

    public Result getAppointmentDate(Long reportStart, Long reportEnd) {
        Date start = new Date(reportStart);
        Date end = new Date(reportEnd);
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsByDate(start, end);
        Map<String, Map<String, Integer>> data = new HashMap<>();
        Map<String, Integer> appointmentType = new HashMap<>();
        Map<String, Integer> appointmentService = new HashMap<>();
        Map<String, Integer> appointmentWeekly = new HashMap<>();
        int weekly = 0;
        int oneTime = 0;
        for (AppointmentsModel a : appointments) {
            appointmentType.merge(a.getAppointmentType(), 1, Integer::sum);
            appointmentService.merge(a.getServiceType(), 1, Integer::sum);
            if (a.isWeekly()) {
                weekly++;
            } else {
                oneTime++;
            }
        }
        appointmentWeekly.put("Weekly", weekly);
        appointmentWeekly.put("OneTime", oneTime);
        data.put("Type", appointmentType);
        data.put("Service", appointmentService);
        data.put("Weekly", appointmentWeekly);
        return ok(Json.toJson(data));
    }
}
