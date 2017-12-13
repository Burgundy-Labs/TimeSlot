package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Databases.SettingsDB;
import models.AppointmentTypeModel;
import models.ServiceModel;
import models.SettingsModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SettingsController extends Controller {

    public Result index() {
        return ok(views.html.settings.render());
    }

    public Result updateSettings() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        SettingsModel settings = new SettingsModel();
        settings.setCenterName(json.findPath("centerName").asText());
        settings.setUniversityName(json.findPath("universityName").asText());
        settings.setSemesterStart(DatatypeConverter.parseDateTime(json.findPath("semesterStart").textValue()).getTime());
        settings.setSemesterEnd(DatatypeConverter.parseDateTime(json.findPath("semesterEnd").textValue()).getTime());
        DateFormat format = new SimpleDateFormat("hh:mm");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = format.parse(json.findPath("startTime").asText());
            endTime = format.parse(json.findPath("endTime").asText());
            if ( endTime.before(startTime) || startTime.after(endTime) || startTime.equals(endTime) ) {
                throw new Exception("The start time was set before or at the same time as the end time.");
            }
            settings.setStartTime(startTime);
            settings.setEndTime(endTime);
            /* Check if user is in DB */
            SettingsDB.changeSettings(settings);
            return ok();
        }
        catch (ParseException e) { e.printStackTrace(); }
        catch (Exception e) { e.printStackTrace(); }
        return notAcceptable();
    }

    public Result createAppointmentType() {
        JsonNode json = request().body().asJson();
        String appointmentTypeName = json.findPath("appointmentTypeName").asText();
        AppointmentTypeModel appointmentType = new AppointmentTypeModel(null, appointmentTypeName);
        SettingsDB.addAppointmentType(appointmentType);
        return ok();
    }

    public static List<AppointmentTypeModel> getAppointmentTypes() {
        return SettingsDB.getAppointmentTypes();
    }

    public Result createService() {
        JsonNode json = request().body().asJson();
        String serviceName = json.findPath("serviceName").asText();
        ServiceModel service = new ServiceModel(null, serviceName);
        SettingsDB.addService(service);
        return ok();
    }

    public Result removeAppointmentType() {
        JsonNode json = request().body().asJson();
        String appointmentTypeId = json.findPath("appointmentTypeId").asText();
        SettingsDB.removeAppointmentType(appointmentTypeId);
        return ok();
    }

    public Result removeService() {
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
