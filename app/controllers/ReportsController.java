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
        UsersModel currentUser = UserController.getCurrentUser();
        if( currentUser ==  null || !currentUser.getRole().equals("Admin")){
            if(session("newUser") != null && session("newUser").equals("true")){
                return ok(views.html.dashboard.render()).withCookies(Http.Cookie.builder("newUser", "true").build());
            } else {
                return ok(views.html.dashboard.render());
            }
        } else {
            return ok(views.html.reports.render());
        }
    }

    public Result appointmentTypeStatistics(Long reportStart, Long reportEnd) {
        Date start = new Date(reportStart);
        Date end = new Date(reportEnd);
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsByDate(start, end);
        Map<String, Integer> appointmentTypeCounts = new HashMap<String, Integer>();
        int count = 0;
        String type;

        for (AppointmentsModel a: appointments ) {
            type = a.getAppointmentType();
            if (appointmentTypeCounts.containsKey(type)){
                count = appointmentTypeCounts.get(type);
                appointmentTypeCounts.put(type,count++);
            } else {
                appointmentTypeCounts.put(type,1);
            }
            count = 0;
        }
        return ok(Json.toJson(appointmentTypeCounts));
    }

    public Result serviceStatistics(Long reportStart, Long reportEnd) {
        Date start = new Date(reportStart);
        Date end = new Date(reportEnd);
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsByDate(start ,end);
        Map<String, Integer> serviceCounts = new HashMap<String,Integer>();
        int count = 0;
        String type;
        for (AppointmentsModel a: appointments ) {
            type = a.getServiceType();
            if (serviceCounts.containsKey(type)){
                count = serviceCounts.get(type);
                serviceCounts.put(type, count++);
            } else {
                serviceCounts.put(type,1);
            }
            count = 0;

        }
        return ok(Json.toJson(serviceCounts));
    }
    public Result weeklyAppointmentStatistics(Long reportStart, Long reportEnd) {
        Date start = new Date(reportStart);
        Date end = new Date(reportEnd);
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsByDate(start, end);
        Map<String,Integer> weeklyCount = new HashMap<String, Integer>();

        int weekly = 0;
        int oneTime = 0;
        for( AppointmentsModel a: appointments){
            if(a.isWeekly()){
                weekly++;
            }else {
                oneTime++;
            }
        }
        weeklyCount.put("Weekly",weekly);
        weeklyCount.put("One-Time", oneTime);
        return ok(Json.toJson(weeklyCount));

    }
}
