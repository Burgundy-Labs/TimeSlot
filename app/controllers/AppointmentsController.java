package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationComponents.MailerService;
import controllers.Databases.AppointmentsDB;
import controllers.Databases.SettingsDB;
import models.AppointmentsModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
import java.util.*;

public class AppointmentsController extends Controller {

    public Result index() {
        String currentRole = UserController.getCurrentRole();
        /* Force redirect to Login is the user isn't signed in */
        if(currentRole == null) {
            return unauthorized(views.html.error_pages.unauthorized.render());
        }
        return ok(views.html.appointments.render());
    }

    public Result makeAppointment() {
        return ok(views.html.makeAppointment.render());
    }

    public Result updatePresence() {
        JsonNode json = request().body().asJson();
        AppointmentsModel appointment = AppointmentsDB.getAppointment(json.findPath("appointmentId").textValue());
        appointment.setPresent(json.findPath("present").asBoolean());
        AppointmentsDB.addAppointment(appointment);
        return ok();
    }

    public Result createAppointment() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        String uniqueId = UUID.randomUUID().toString();
        AppointmentsModel appointment = new AppointmentsModel();
        appointment.setCoachId(json.findPath("coachId").textValue());
        appointment.setStudentId(json.findPath("studentId").textValue());
        appointment.setAppointmentType(json.findPath("appointmentType").textValue());
        appointment.setStartDate(DatatypeConverter.parseDateTime(json.findPath("startDate").textValue()).getTime());
        appointment.setEndDate(DatatypeConverter.parseDateTime(json.findPath("endDate").textValue()).getTime());
        appointment.setAppointmentNotes(json.findPath("appointmentNotes").textValue());
        appointment.setPresent(Boolean.getBoolean(json.findPath("present").textValue()));
        appointment.setServiceType(json.findPath("serviceType").textValue());
        appointment.setWeekly(json.findPath("weekly").asBoolean());
        if ( appointment.isWeekly() ) {
            appointment.setWeeklyId(uniqueId);
            new Thread(() -> createWeeklyAppointments(json, uniqueId)).start();
        } else {
            appointment.setWeeklyId("");
        }
        appointment = AppointmentsDB.addAppointment(appointment);
        /* Check if user is in DB */
        AppointmentsModel finalAppointment = appointment;
        new Thread(() -> MailerService.sendAppointmentConfirmation(finalAppointment)).start();
        return ok();
    }

    private void createWeeklyAppointments(JsonNode json, String uniqueId){
        Calendar currentDate = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue());
        Calendar endDate = DatatypeConverter.parseDateTime(json.findPath("endDate").textValue());
        Calendar semesterEnd = Calendar.getInstance();
        semesterEnd.setTime(SettingsDB.getSettings().getSemesterEnd());
        currentDate.add(Calendar.DAY_OF_YEAR, 7);
        endDate.add(Calendar.DAY_OF_YEAR, 7);
        while ( currentDate.before(semesterEnd) ){
            AppointmentsModel newAppointment = new AppointmentsModel();
            newAppointment.setCoachId(json.findPath("coachId").textValue());
            newAppointment.setStudentId(json.findPath("studentId").textValue());
            newAppointment.setAppointmentType(json.findPath("appointmentType").textValue());
            newAppointment.setStartDate(currentDate.getTime());
            newAppointment.setEndDate(endDate.getTime());
            newAppointment.setAppointmentNotes(json.findPath("appointmentNotes").textValue());
            newAppointment.setPresent(Boolean.getBoolean(json.findPath("present").textValue()));
            newAppointment.setServiceType(json.findPath("serviceType").textValue());
            newAppointment.setWeekly(json.findPath("weekly").asBoolean());
            newAppointment.setWeeklyId(uniqueId);
            AppointmentsDB.addAppointment(newAppointment);
            currentDate.add(Calendar.DAY_OF_YEAR, 7);
            endDate.add(Calendar.DAY_OF_YEAR, 7);
        }
    }

    public Result cancelAppointment(){
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        AppointmentsModel appointment = AppointmentsDB.removeAppointment(appointmentId);
        if ( json.findPath("weeklyId").asText() != null && json.findPath("weeklyId").asText() != "" ) {
            List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsForUser("Student", appointment.getStudentId());
            for ( AppointmentsModel ap : appointments ) {
                if ( ap.getWeeklyId() != null && ap.getWeeklyId().equals(json.findPath("weeklyId").asText()) ) {
                    AppointmentsDB.removeAppointment(ap.getAppointmentId());
                }
            }
        }
        new Thread(() -> MailerService.sendAppointmentCancellation(appointment, json.findPath("cancelNotes").asText())).start();
        return ok();
    }

    public Result updateCoachNotes(){
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        String coachNotes = json.findPath("coachNotes").textValue();
        AppointmentsModel appointment = AppointmentsDB.getAppointment(appointmentId);
        appointment.setCoachNotes(coachNotes);
        AppointmentsDB.addAppointment(appointment);
        return ok();
    }

    public Result appointmentsForUser(String role, String userId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsByUserAndDate(userId, startDate, endDate);
        return ok(Json.toJson(appointments));
    }
}
