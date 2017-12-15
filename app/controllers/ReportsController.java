package controllers;

import controllers.Databases.AppointmentsDB;
import controllers.Databases.SettingsDB;
import models.AppointmentTypeModel;
import models.AppointmentsModel;
import models.ServiceModel;
import models.UsersModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;
import java.util.stream.Collectors;

public class ReportsController extends Controller {

    public Result index() {
        UsersModel currentUser = UserController.getCurrentUser();
        if( currentUser ==  null || !currentUser.getRole().equals("Admin")){
            return ok(views.html.dashboard.render());
        } else {
            return ok(views.html.reports.render());
        }
    }

    public Result appointmentTypeStatistics(Long reportStart, Long reportEnd) {
        Date start = new Date(reportStart);
        Date end = new Date(reportEnd);
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsByDate(start, end);
        List<AppointmentTypeModel> appointmentTypes = SettingsDB.getAppointmentTypes();
        Map<String, Integer> appointmentTypeCounts = appointmentTypes.stream().collect(Collectors.toMap(AppointmentTypeModel::getAppointmentType, i -> 0));

        for (AppointmentsModel a: appointments ) {
            appointmentTypeCounts.put(a.getAppointmentType(), appointmentTypeCounts.get(a.getAppointmentType()) + 1);
        }
        return ok(Json.toJson(appointmentTypeCounts));
    }

    public Result serviceStatistics(Long reportStart, Long reportEnd) {
        Date start = new Date(reportStart);
        Date end = new Date(reportEnd);
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsByDate(start,end);
        List<ServiceModel> services = SettingsDB.getServices();
        Map<String, Integer> serviceCounts = services.stream().collect(Collectors.toMap(ServiceModel::getService, i -> 0));

        for (AppointmentsModel a: appointments ) {
            serviceCounts.put(a.getServiceType(), serviceCounts.get(a.getServiceType()) + 1);
        }
        return ok(Json.toJson(serviceCounts));
    }
}
