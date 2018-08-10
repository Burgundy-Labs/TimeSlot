package controllers;

import application_components.annotations.Authenticate;
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
    private SettingsDB settingsDB = new SettingsDB();
    private MailerService mailerService = new MailerService();

    @Authenticate
    public Result index() {
        return ok(views.html.pages.appointments.render());
    }

    @Authenticate(role = "Coach")
    public Result updatePresence() {
        JsonNode json = request().body().asJson();
        AppointmentsModel appointment = appointmentsDB.get(json.findPath("appointmentId").textValue()).orElseThrow(NullPointerException::new);
        appointment.setPresent(json.findPath("present").asBoolean());
        appointmentsDB.addOrUpdate(appointment);
        return ok();
    }

    @Authenticate
    public Result createAppointment() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        String uniqueId = UUID.randomUUID().toString();
        AppointmentsModel appointment = new AppointmentsModel();
        appointment.setCoachId(json.findPath("userId").textValue());
        appointment.setStudentId(null);
        appointment.setAppointmentType("");
        appointment.setStartDate(DatatypeConverter.parseDateTime(json.findPath("startDate").textValue()).getTime());
        appointment.setEndDate(DatatypeConverter.parseDateTime(json.findPath("endDate").textValue()).getTime());
        appointment.setAppointmentNotes("");
        appointment.setPresent(false);
        appointment.setServiceType("");
        appointment.setWeekly(json.findPath("weekly").asBoolean());
        if (appointment.isWeekly()) {
            appointment.setWeeklyId(uniqueId);
            new Thread(() -> createWeeklyAppointments(json, uniqueId)).start();
        } else {
            appointment.setWeeklyId("");
        }
        appointmentsDB.addOrUpdate(appointment);
        /* Check if user is in DB */

        // new Thread(() -> mailerService.sendAppointmentConfirmation(appointment)).start();

        return ok();
    }

    @Authenticate
    private void createWeeklyAppointments(JsonNode json, String uniqueId) {
        Calendar startDate = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue());
        Calendar currentDate = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue());
        Calendar endDate = DatatypeConverter.parseDateTime(json.findPath("endDate").textValue());
        Calendar semesterEnd = Calendar.getInstance();
        semesterEnd.setTime(settingsDB.get(null).orElseThrow(NullPointerException::new).getSemesterEnd());
        currentDate.add(Calendar.DAY_OF_YEAR, 7);
        endDate.add(Calendar.DAY_OF_YEAR, 7);
        while (currentDate.before(semesterEnd)) {
            AppointmentsModel newAppointment = new AppointmentsModel();
            newAppointment.setCoachId(json.findPath("userId").textValue());
            newAppointment.setStudentId(null);
            newAppointment.setAppointmentType("");

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
            newAppointment.setAppointmentNotes("");
            newAppointment.setPresent(false);
            newAppointment.setServiceType("");
            newAppointment.setWeekly(json.findPath("weekly").asBoolean());
            newAppointment.setWeeklyId(uniqueId);
            appointmentsDB.addOrUpdate(newAppointment);
            currentDate.add(Calendar.DAY_OF_YEAR, 7);
            endDate.add(Calendar.DAY_OF_YEAR, 7);
        }
    }

    public Result cancelAppointment() {
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        AppointmentsModel appointment = appointmentsDB.remove(appointmentId).orElseThrow(NullPointerException::new);
        if (appointment.isWeekly()) {
            List<AppointmentsModel> appointments = appointmentsDB.getAppointmentsForUser("Student", appointment.getStudentId());
            for (AppointmentsModel ap : appointments) {
                if (ap.getWeeklyId() != null && ap.getWeeklyId().equals(json.findPath("weeklyId").asText()) && ap.getStartDate().after(new Date())) {
                    appointmentsDB.remove(ap.getAppointmentId());
                }
            }
        }
        new Thread(() -> mailerService.sendAppointmentCancellation(appointment, json.findPath("cancelNotes").asText())).start();
        return ok();
    }

    public Result removeAppointment() {
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        Boolean removeAll = json.findPath("removeAll").asBoolean();
        AppointmentsModel appointment = appointmentsDB.remove(appointmentId).orElseThrow(NullPointerException::new);
        if (appointment.isWeekly() && removeAll) {
            List<AppointmentsModel> appointments = appointmentsDB.getByWeeklyId(appointment.getWeeklyId());
            for (AppointmentsModel ap : appointments) {
                if (ap.getStartDate().after(new Date()) && appointment.getStudentId() == null) {
                    appointmentsDB.remove(ap.getAppointmentId());
                }
            }
        }
        return ok();
    }

    public Result updateCoachNotes() {
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        String coachNotes = json.findPath("coachNotes").textValue();
        AppointmentsModel appointment = appointmentsDB.get(appointmentId).orElseThrow(NullPointerException::new);
        appointment.setCoachNotes(coachNotes);
        appointmentsDB.addOrUpdate(appointment);
        return ok();
    }

    public Result appointmentsForUser(String role, String userId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        Calendar endb = Calendar.getInstance();
        endb.setTime(endDate);
        endb.set(Calendar.HOUR_OF_DAY, 24);
        endDate = endb.getTime();
        List<AppointmentsModel> appointments = appointmentsDB.getAppointmentsByUserAndDate(userId, startDate, endDate);
        return ok(Json.toJson(appointments));
    }

    public Result appointmentsAsCoach(String userId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        Calendar endc = Calendar.getInstance();
        endc.setTime(endDate);
        endc.set(Calendar.HOUR_OF_DAY, 24);
        endDate = endc.getTime();
        List<AppointmentsModel> appointments = appointmentsDB.getCoachAvailablitiliy(userId, startDate, endDate);
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
