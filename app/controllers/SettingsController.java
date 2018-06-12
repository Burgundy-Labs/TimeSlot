package controllers;

import application_components.annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import databases.SettingsDB;
import models.AppointmentTypeModel;
import models.ServiceModel;
import models.SettingsModel;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SettingsController extends Controller {
    private SettingsDB settingsDB = new SettingsDB();

    @Authenticate(role="Admin")
    public Result index() {
        return ok(views.html.pages.settings.render());
    }

    @Authenticate(role="Admin")
    public Result appointmentTypeFrequency() {
        JsonNode json = request().body().asJson();
        String frequency = json.findPath("frequency").asText();
        Boolean enabled = json.findPath("checked").asBoolean();
        String appointmentTypeId = json.findPath("id").asText();
        AppointmentTypeModel appointmentType = settingsDB.getAppointmentType(appointmentTypeId);
        if("oneTime".equals(frequency)) {
            appointmentType.setOneTime(enabled);
        } else if("weekly".equals(frequency)){
            appointmentType.setWeekly(enabled);
        }
        settingsDB.addAppointmentType(appointmentType);
        return ok();
    }

    @Authenticate(role="Admin")
    public Result changeSiteAlert(){
        JsonNode json = request().body().asJson();
        String siteAlert = json.findPath("siteAlert").asText();
        SettingsModel s = getSettings();
        s.setSiteAlert(siteAlert);
        settingsDB.addOrUpdate(s);
        return ok();
    }

    @Authenticate(role="Admin")
    public Result changeCenterInformation(){
        JsonNode json = request().body().asJson();
        String centerInformation = json.findPath("centerInformation").asText();
        SettingsModel s = getSettings();
        s.setCenterInformation(centerInformation);
        settingsDB.addOrUpdate(s);
        return ok();
    }

    @Authenticate(role="Admin")
    public Result changeMaximumAppointments() {
        JsonNode json = request().body().asJson();
        Integer maximumAppointments = json.findPath("maximumAppointments").asInt();
        SettingsModel s = getSettings();
        s.setMaximumAppointments(maximumAppointments);
        settingsDB.addOrUpdate(s);
        return ok();
    }

    @Authenticate(role="Admin")
    public Result updateSettings() {
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
            settings.setMaximumAppointments(json.findPath("maxAppointments").asInt());
            boolean[] daysOfWeek = {json.findPath("sunday").asBoolean(),
                    json.findPath("monday").asBoolean(),
                    json.findPath("tuesday").asBoolean(),
                    json.findPath("wednesday").asBoolean(),
                    json.findPath("thursday").asBoolean(),
                    json.findPath("friday").asBoolean(),
                    json.findPath("saturday").asBoolean()};
            settings.setDaysOpenWeekly(daysOfWeek);
            /* Check if user is in DB */
            settingsDB.addOrUpdate(settings);
            return ok();
        } catch (Exception e) { e.printStackTrace(); }
        return notAcceptable();
    }

    @Authenticate(role="Admin")
    public Result createAppointmentType() {
        JsonNode json = request().body().asJson();
        String appointmentTypeName = json.findPath("appointmentTypeName").asText();
        AppointmentTypeModel appointmentType = new AppointmentTypeModel(null, appointmentTypeName);
        settingsDB.addAppointmentType(appointmentType);
        return ok();
    }

    public List<AppointmentTypeModel> getAppointmentTypes() {
        return settingsDB.getAppointmentTypes();
    }

    public List<AppointmentTypeModel> getAvailableAppointmentTypes() {
        List<AppointmentTypeModel> appointmentTypes = settingsDB.getAppointmentTypes();
        appointmentTypes.removeIf(a -> (!a.getOneTime() && !a.getWeekly()));
        return appointmentTypes;
    }

    @Authenticate(role="Admin")
    public Result createService() {
        JsonNode json = request().body().asJson();
        String serviceName = json.findPath("serviceName").asText();
        ServiceModel service = new ServiceModel(null, serviceName);
        settingsDB.addService(service);
        return ok();
    }

    @Authenticate(role="Admin")
    public Result removeAppointmentType() {
        JsonNode json = request().body().asJson();
        String appointmentTypeId = json.findPath("appointmentTypeId").asText();
        settingsDB.removeAppointmentType(appointmentTypeId);
        return ok();
    }

    @Authenticate(role="Admin")
    public Result removeService() {
        JsonNode json = request().body().asJson();
        String serviceId = json.findPath("serviceId").asText();
        settingsDB.removeService(serviceId);
        return ok();
    }

    public List<ServiceModel> getServices() {
        return settingsDB.getServices();
    }

    public SettingsModel getSettings() {
        return settingsDB.get(null).orElseThrow(NullPointerException::new);
    }

    public boolean getAppointmentTypeOneTime(String appointmentType) { return settingsDB.getAppointmentTypeByName(appointmentType).getOneTime(); }

    public boolean getAppointmentTypeWeekly(String appointmentType) { return settingsDB.getAppointmentTypeByName(appointmentType).getWeekly(); }

}
