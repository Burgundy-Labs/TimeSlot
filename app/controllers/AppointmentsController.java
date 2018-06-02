package controllers;

import application_components.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import application_components.mailing.MailerService;
import databases.AppointmentsDB;
import databases.SettingsDB;
import models.AppointmentsModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
import java.util.*;

public class AppointmentsController extends Controller {
    private AppointmentsDB appointmentsDB = new AppointmentsDB();
    private MailerService mailerService = new MailerService();

    @Authenticate
    public Result index() {
        return ok(views.html.appointments.render());
    }

    public Result updatePresence() {
        JsonNode json = request().body().asJson();
        AppointmentsModel appointment = appointmentsDB.getAppointment(json.findPath("appointmentId").textValue());
        appointment.setPresent(json.findPath("present").asBoolean());
        appointmentsDB.addAppointment(appointment);
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
        if (appointment.isWeekly()) {
            appointment.setWeeklyId(uniqueId);
            new Thread(() -> createWeeklyAppointments(json, uniqueId)).start();
        } else {
            appointment.setWeeklyId("");
        }
        appointment = appointmentsDB.addAppointment(appointment);
        /* Check if user is in DB */
        AppointmentsModel finalAppointment = appointment;
        new Thread(() -> mailerService.sendAppointmentConfirmation(finalAppointment)).start();
        return ok();
    }

    private void createWeeklyAppointments(JsonNode json, String uniqueId) {
        Calendar startDate = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue());
        Calendar currentDate = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue());
        Calendar endDate = DatatypeConverter.parseDateTime(json.findPath("endDate").textValue());
        Calendar semesterEnd = Calendar.getInstance();
        semesterEnd.setTime(SettingsDB.getSettings().getSemesterEnd());
        currentDate.add(Calendar.DAY_OF_YEAR, 7);
        endDate.add(Calendar.DAY_OF_YEAR, 7);
        while (currentDate.before(semesterEnd)) {
            AppointmentsModel newAppointment = new AppointmentsModel();
            newAppointment.setCoachId(json.findPath("coachId").textValue());
            newAppointment.setStudentId(json.findPath("studentId").textValue());
            newAppointment.setAppointmentType(json.findPath("appointmentType").textValue());

            Calendar startWeeklyDate = Calendar.getInstance();
            startWeeklyDate.setTime(currentDate.getTime());
            Calendar endWeeklyDate = Calendar.getInstance();
            endWeeklyDate.setTime(endDate.getTime());

            if (!TimeZone.getTimeZone( "US/Michigan").inDaylightTime( startDate.getTime() ) && TimeZone.getTimeZone( "US/Michigan").inDaylightTime( currentDate.getTime() )) {
                startWeeklyDate.add(Calendar.HOUR_OF_DAY, -1);
                endWeeklyDate.add(Calendar.HOUR_OF_DAY, -1);
            } else if (TimeZone.getTimeZone( "US/Michigan").inDaylightTime( startDate.getTime() ) && !TimeZone.getTimeZone( "US/Michigan").inDaylightTime( currentDate.getTime() )) {
                startWeeklyDate.add(Calendar.HOUR_OF_DAY, 1);
                endWeeklyDate.add(Calendar.HOUR_OF_DAY, 1);
            }

            newAppointment.setStartDate(startWeeklyDate.getTime());
            newAppointment.setEndDate(endWeeklyDate.getTime());
            newAppointment.setAppointmentNotes(json.findPath("appointmentNotes").textValue());
            newAppointment.setPresent(Boolean.getBoolean(json.findPath("present").textValue()));
            newAppointment.setServiceType(json.findPath("serviceType").textValue());
            newAppointment.setWeekly(json.findPath("weekly").asBoolean());
            newAppointment.setWeeklyId(uniqueId);
            appointmentsDB.addAppointment(newAppointment);
            currentDate.add(Calendar.DAY_OF_YEAR, 7);
            endDate.add(Calendar.DAY_OF_YEAR, 7);
        }
    }

    public Result cancelAppointment() {
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        AppointmentsModel appointment = appointmentsDB.removeAppointment(appointmentId);
        if (appointment.isWeekly()) {
            List<AppointmentsModel> appointments = appointmentsDB.getAppointmentsForUser("Student", appointment.getStudentId());
            for (AppointmentsModel ap : appointments) {
                if (ap.getWeeklyId() != null && ap.getWeeklyId().equals(json.findPath("weeklyId").asText()) && ap.getStartDate().after(new Date())) {
                    appointmentsDB.removeAppointment(ap.getAppointmentId());
                }
            }
        }
        new Thread(() -> mailerService.sendAppointmentCancellation(appointment, json.findPath("cancelNotes").asText())).start();
        return ok();
    }

    public Result updateCoachNotes() {
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        String coachNotes = json.findPath("coachNotes").textValue();
        AppointmentsModel appointment = appointmentsDB.getAppointment(appointmentId);
        appointment.setCoachNotes(coachNotes);
        appointmentsDB.addAppointment(appointment);
        return ok();
    }

    public Result appointmentsForUser(String role, String userId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        Calendar endc = Calendar.getInstance();
        endc.setTime(endDate);
        endc.set(Calendar.HOUR_OF_DAY, 24);
        endDate = endc.getTime();
        List<AppointmentsModel> appointments = appointmentsDB.getAppointmentsByUserAndDate(userId, startDate, endDate);
        return ok(Json.toJson(appointments));
    }

    @Authenticate(role="Admin")
    public Result appointmentsByDate(String role, String userId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        Calendar endc = Calendar.getInstance();
        endc.setTime(endDate);
        endc.set(Calendar.HOUR_OF_DAY, 24);
        endDate = endc.getTime();
        List<AppointmentsModel> appointments = appointmentsDB.getAppointmentsByDate(startDate, endDate);
        return ok(Json.toJson(appointments));
    }
}
