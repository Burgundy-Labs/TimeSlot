package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Databases.SettingsDB;
import models.AppointmentTypeModel;
import models.ServiceModel;
import models.SettingsModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class SettingsController extends Controller {

    public Result index() {
        return ok(views.html.settings.render());
    }

    public Result updateSettings() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        SettingsModel settings = Json.fromJson(json, SettingsModel.class);
        /* Check if user is in DB */
        SettingsDB.changeSettings(settings);
        return ok();
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
