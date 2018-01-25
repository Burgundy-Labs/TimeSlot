package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Databases.SettingsDB;
import models.AppointmentTypeModel;
import models.ServiceModel;
import models.SettingsModel;
import models.UsersModel;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SettingsController extends Controller {

    public Result index() {
        String currentRole = UserController.getCurrentRole();
        if( currentRole ==  null || !currentRole.equals("Admin")){
            if(session("newUser") != null && session("newUser").equals("true")){
                return ok(views.html.dashboard.render()).withCookies(Http.Cookie.builder("newUser", "true").build());
            } else {
                return ok(views.html.dashboard.render());
            }
        } else {
            return ok(views.html.settings.render());
        }
    }

    public Result appointmentTypeFrequency() {
        if(!UserController.getCurrentRole().equals("Admin")){
            return unauthorized();
        }
        JsonNode json = request().body().asJson();
        String frequency = json.findPath("frequency").asText();
        Boolean enabled = json.findPath("checked").asBoolean();
        String appointmentTypeId = json.findPath("id").asText();
        AppointmentTypeModel appointmentType = SettingsDB.getAppointmentType(appointmentTypeId);
        if(frequency.equals("oneTime")) {
            appointmentType.setOneTime(enabled);
        } else if(frequency.equals("weekly")){
            appointmentType.setWeekly(enabled);
        }
        SettingsDB.addAppointmentType(appointmentType);
        return ok();
    }
    public Result changeSiteAlert(){
        if(!UserController.getCurrentRole().equals("Admin")){
            return unauthorized();
        }
        JsonNode json = request().body().asJson();
        String siteAlert = json.findPath("siteAlert").asText();
        SettingsModel s = SettingsDB.getSettings();
        s.setSiteAlert(siteAlert);
        SettingsDB.changeSettings(s);
        return ok();
    }

    public Result updateSettings() {
        if(!UserController.getCurrentRole().equals("Admin")){
            return unauthorized();
        }
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        SettingsModel settings = new SettingsModel();
        try {
            settings.setCenterName(json.findPath("centerName").asText());
            settings.setUniversityName(json.findPath("universityName").asText());
            Date startDate = DatatypeConverter.parseDateTime(json.findPath("semesterStart").textValue()).getTime();
            Date endDate = DatatypeConverter.parseDateTime(json.findPath("semesterEnd").textValue()).getTime();
            settings.setSemesterStart(startDate);
            settings.setSemesterEnd(endDate);
            DateFormat format = new SimpleDateFormat("HH:mm");
            Date startTime = format.parse(json.findPath("startTime").asText());
            Date endTime = format.parse(json.findPath("endTime").asText());
            if ( endTime.before(startTime) || startTime.after(endTime) || startTime.equals(endTime) ) {
                throw new Exception("The start time was set before or at the same time as the end time.");
            }
            if ( endDate.before(startDate) || startDate.after(endDate) || startDate.equals(endDate) ) {
                throw new Exception("The start date was set before or at the same date as the end date.");
            }
            settings.setStartTime(startTime);
            settings.setEndTime(endTime);
            /* Check if user is in DB */
            SettingsDB.changeSettings(settings);
            return ok();
        } catch (Exception e) { e.printStackTrace(); }
        return notAcceptable();
    }

    public Result createAppointmentType() {
        if(!UserController.getCurrentRole().equals("Admin")){
            return unauthorized();
        }
        JsonNode json = request().body().asJson();
        String appointmentTypeName = json.findPath("appointmentTypeName").asText();
        AppointmentTypeModel appointmentType = new AppointmentTypeModel(null, appointmentTypeName);
        SettingsDB.addAppointmentType(appointmentType);
        return ok();
    }

    public static List<AppointmentTypeModel> getAppointmentTypes() {
        return SettingsDB.getAppointmentTypes();
    }

    public static List<AppointmentTypeModel> getAvailableAppointmentTypes() {
        List<AppointmentTypeModel> appointmentTypes = SettingsDB.getAppointmentTypes();
        appointmentTypes.removeIf(a -> (!a.getOneTime() && !a.getWeekly()));
        return appointmentTypes;
    }

    public Result createService() {
        if(!UserController.getCurrentRole().equals("Admin")){
            return unauthorized();
        }
        JsonNode json = request().body().asJson();
        String serviceName = json.findPath("serviceName").asText();
        ServiceModel service = new ServiceModel(null, serviceName);
        SettingsDB.addService(service);
        return ok();
    }

    public Result removeAppointmentType() {
        if(!UserController.getCurrentRole().equals("Admin")){
            return unauthorized();
        }
        JsonNode json = request().body().asJson();
        String appointmentTypeId = json.findPath("appointmentTypeId").asText();
        SettingsDB.removeAppointmentType(appointmentTypeId);
        return ok();
    }

    public Result removeService() {
        if(!UserController.getCurrentRole().equals("Admin")){
            return unauthorized();
        }
        JsonNode json = request().body().asJson();
        String serviceId = json.findPath("serviceId").asText();
        SettingsDB.removeService(serviceId);
        return ok();
    }

    public static List<ServiceModel> getServices() {
        return SettingsDB.getServices();
    }

    public static SettingsModel getSettings() {
        return  SettingsDB.getSettings();
    }

}
