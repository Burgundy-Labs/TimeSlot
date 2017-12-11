package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationComponents.AppointmentTypes;
import controllers.Databases.AppointmentsDB;
import controllers.Databases.AvailabilityDB;
import models.AppointmentsModel;
import models.AvailabilityModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
import java.util.*;

public class AppointmentsController extends Controller {

    public Result index() {
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
        AppointmentsModel appointment = new AppointmentsModel();
        appointment.setCoachId(json.findPath("coachId").textValue());
        appointment.setStudentId(json.findPath("studentId").textValue());
        appointment.setAppointmentType(json.findPath("appointmentType").textValue());
        appointment.setStartDate(DatatypeConverter.parseDateTime(json.findPath("startDate").textValue()).getTime());
        appointment.setEndDate(DatatypeConverter.parseDateTime(json.findPath("endDate").textValue()).getTime());
        appointment.setAppointmentNotes(json.findPath("appointmentNotes").textValue());
        appointment.setPresent(Boolean.getBoolean(json.findPath("present").textValue()));
        appointment.setServiceType(json.findPath("serviceType").textValue());
        /* Check if user is in DB */
        appointment = AppointmentsDB.addAppointment(appointment);
        AppointmentsModel finalAppointment = appointment;
        new Thread(() -> MailerService.sendAppointmentConfirmation(finalAppointment)).start();
        return ok();
    }

    public Result cancelAppointment(){
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        AppointmentsModel appointment = AppointmentsDB.removeAppointment(appointmentId);
        MailerService.sendAppointmentCancellation(appointment);
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

    public Result appointmentsForUser(String role, String userId) {
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsForUser(role, userId);
        return ok(Json.toJson(appointments));
    }
}
