package controllers;

import controllers.Databases.AppointmentsDB;
import controllers.Databases.SettingsDB;
import models.AppointmentTypeModel;
import models.AppointmentsModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportsController extends Controller {

    public Result index() {
        return ok(views.html.reports.render());
    }

    public Result appointmentTypeStatistics() {
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointments();
        List<AppointmentTypeModel> appointmentTypes = SettingsDB.getAppointmentTypes();
        Map<String, Integer> appointmentTypeCounts = appointmentTypes.stream().collect(Collectors.toMap(AppointmentTypeModel::getAppointmentType, i -> 0));

        for (AppointmentsModel a: appointments ) {
            appointmentTypeCounts.put(a.getAppointmentType(), appointmentTypeCounts.get(a.getAppointmentType()) + 1);
        }
        return ok(Json.toJson(appointmentTypeCounts));
    }
}
