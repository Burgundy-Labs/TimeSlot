package controllers;

import application_components.annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import application_components.mailing.MailerService;
import databases.AppointmentsDB;
import databases.SettingsDB;
import databases.UserDB;
import models.*;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import play.cache.Cached;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AppointmentsController extends BaseController {
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
        return ok(Json.toJson(appointment));
    }

    @Authenticate
    public Result createAppointment() throws Exception {
        JsonNode json = request().body().asJson();
        AppointmentsModel availability = appointmentsDB.get(json.findPath("appointmentId").asText()).orElseThrow(NullPointerException::new);
        if(availability.getStudentId() != null) throw new Exception("Appointment is taken");
        Date startDate = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue()).getTime();
        Date endDate = DatatypeConverter.parseDateTime(json.findPath("endDate").textValue()).getTime();
        UsersModel student = userDB.get(json.findPath("studentId").asText()).get();

        AppointmentsModel appointment = availability.clone();
        appointment.setServiceType(json.findPath("serviceType").asText());
        appointment.setAppointmentType(json.findPath("appointmentType").asText());
        appointment.setAppointmentNotes(json.findPath("appointmentNotes").asText());
        appointment.setPresent(false);
        appointment.setStartDate(startDate);
        appointment.setEndDate(endDate);

        if (!json.findPath("weekly").asBoolean()) {
            appointment.setWeeklyId(null);
            appointment.setWeekly(false);
        }

        AppointmentsModel emailAppointment = appointment.clone();
        emailAppointment.setStudentData(student.getUid(), student.getEmail(), student.getDisplayName(), student.getPhotoURL());

        if (appointment.isWeekly()) {
            String UUID1 = UUID.randomUUID().toString();
            String UUID2 = UUID.randomUUID().toString();
            String UUID3 = UUID.randomUUID().toString();
            Calendar start = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue());
            Calendar end = DatatypeConverter.parseDateTime(json.findPath("endDate").textValue());

            List<AppointmentsModel> weeklyAvailabilities = appointmentsDB.getByWeeklyId(availability.getWeeklyId());
            Calendar startWeekly = Calendar.getInstance();
            startWeekly.setTime(weeklyAvailabilities.get(0).getStartDate());
            for (AppointmentsModel weeklyAvailability : weeklyAvailabilities) {
                Calendar startAppointment = Calendar.getInstance();
                startAppointment.setTime(weeklyAvailability.getStartDate());
                Calendar endAppointment = Calendar.getInstance();
                endAppointment.setTime(weeklyAvailability.getEndDate());
                startAppointment.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
                startAppointment.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
                endAppointment.set(Calendar.HOUR_OF_DAY, end.get(Calendar.HOUR_OF_DAY));
                endAppointment.set(Calendar.MINUTE, end.get(Calendar.MINUTE));
                appointment.setStartDate(startAppointment.getTime());
                appointment.setEndDate(endAppointment.getTime());
                if ((appointment.getStartDate().after(startDate) && appointment.isWeekly()) || appointment.getStartDate().equals(startDate)) {
                    split(weeklyAvailability, student, appointment.clone(), UUID1, UUID2, UUID3, true);
                } else {
                    split(weeklyAvailability, student, appointment.clone(), UUID1, UUID2, UUID3, false);
                }
            }
        } else {
            split(availability, student, appointment);
        }

        // Send email
        new Thread(() -> mailerService.sendAppointmentConfirmation(emailAppointment)).start();
        return ok();
    }

    private void split(AppointmentsModel availability, UsersModel student, AppointmentsModel appointment) {
        if (availability.getStartDate().before(appointment.getStartDate()) && availability.getEndDate().equals(appointment.getEndDate())) {
            availability.setEndDate(appointment.getStartDate());
            appointment.setAppointmentId(null);
            appointmentsDB.addOrUpdate(availability);
        } else if (availability.getStartDate().equals(appointment.getStartDate()) && availability.getEndDate().after(appointment.getEndDate())) {
            availability.setStartDate(appointment.getEndDate());
            appointment.setAppointmentId(null);
            appointmentsDB.addOrUpdate(availability);
        } else if (availability.getStartDate().before(appointment.getStartDate()) && availability.getEndDate().after(appointment.getEndDate())) {
            AppointmentsModel newAvailability = availability.clone();
            newAvailability.setStartDate(appointment.getEndDate());
            newAvailability.setEndDate(availability.getEndDate());
            newAvailability.setAppointmentId(null);
            availability.setEndDate(appointment.getStartDate());
            appointment.setAppointmentId(null);
            appointmentsDB.addOrUpdate(availability);
            appointmentsDB.addOrUpdate(newAvailability);
        }
        appointment.setStudentData(student.getUid(), student.getEmail(), student.getDisplayName(), student.getPhotoURL());
        appointmentsDB.addOrUpdate(appointment);
    }

    private void split(AppointmentsModel availability, UsersModel student, AppointmentsModel appointment, String UUID1, String UUID2, String UUID3, boolean isAppointment) {
        if (availability.getStartDate().before(appointment.getStartDate()) && availability.getEndDate().equals(appointment.getEndDate())) { // Availability starts before appointment but ends at the same time
            availability.setEndDate(appointment.getStartDate());
            availability.setWeeklyId(UUID3);
            appointment.setAppointmentId(null);
            appointment.setWeeklyId(UUID1);
            availability.setWeekly(true);
            appointmentsDB.addOrUpdate(availability);
        } else if (availability.getStartDate().equals(appointment.getStartDate()) && availability.getEndDate().after(appointment.getEndDate())) { // Avaiilability starts at the same time but ends after
            availability.setStartDate(appointment.getEndDate());
            availability.setWeeklyId(UUID3);
            appointment.setAppointmentId(null);
            availability.setWeekly(true);
            appointment.setWeeklyId(UUID1);
            appointmentsDB.addOrUpdate(availability);
        } else if (availability.getStartDate().before(appointment.getStartDate()) && availability.getEndDate().after(appointment.getEndDate())) { // Availability starts before and after the appoinment
            AppointmentsModel newAvailability = availability.clone();
            newAvailability.setStartDate(appointment.getEndDate());
            newAvailability.setAppointmentId(null);
            availability.setEndDate(appointment.getStartDate());
            appointment.setWeeklyId(UUID1);
            availability.setWeekly(true);
            newAvailability.setWeekly(true);
            appointment.setAppointmentId(null);
            newAvailability.setWeeklyId(UUID2);
            availability.setWeeklyId(UUID3);
            appointmentsDB.addOrUpdate(availability);
            appointmentsDB.addOrUpdate(newAvailability);
        } else {
            if (isAppointment) {
                availability.setStudentData(student.getUid(), student.getEmail(), student.getDisplayName(), student.getPhotoURL());
                availability.setServiceType(appointment.getServiceType());
                availability.setAppointmentType(appointment.getAppointmentType());
                availability.setAppointmentNotes(appointment.getAppointmentNotes());
            } else {
                availability.clearStudentData();
            }
            appointmentsDB.addOrUpdate(availability);
            return;
        }
        if (isAppointment) {
            appointment.setStudentData(student.getUid(), student.getEmail(), student.getDisplayName(), student.getPhotoURL());
        } else {
            appointment.clearStudentData();
        }
        appointmentsDB.addOrUpdate(appointment);
    }

    @Authenticate
    public Result createAvailability() {

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
        boolean weekly = json.findPath("weekly").asBoolean();
        appointment.setWeekly(weekly);
        if (appointment.isWeekly()) {
            appointment.setWeeklyId(uniqueId);
            new Thread(() -> createWeeklyAvailabilities(json, uniqueId)).start();
        } else {
            appointment.setWeeklyId("");
        }
        appointmentsDB.addOrUpdate(appointment);
        return ok();
    }

    @Authenticate
    private void createWeeklyAvailabilities(JsonNode json, String uniqueId) {
        Calendar startDate = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue());
        Calendar currentStart = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue());
        Calendar endDate = DatatypeConverter.parseDateTime(json.findPath("endDate").textValue());
        Calendar currentEnd = DatatypeConverter.parseDateTime(json.findPath("endDate").textValue());
        Calendar semesterEnd = Calendar.getInstance();
        semesterEnd.setTime(settingsDB.get(null).orElseThrow(NullPointerException::new).getSemesterEnd());
        currentStart.add(Calendar.DAY_OF_YEAR, 7);
        currentEnd.add(Calendar.DAY_OF_YEAR, 7);
        while (currentStart.before(semesterEnd)) {
            currentStart.set(Calendar.HOUR_OF_DAY, startDate.get(Calendar.HOUR_OF_DAY));
            currentStart.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
            currentEnd.set(Calendar.HOUR_OF_DAY, endDate.get(Calendar.HOUR_OF_DAY));
            currentEnd.set(Calendar.MINUTE, endDate.get(Calendar.MINUTE));
            AppointmentsModel newAppointment = new AppointmentsModel();
            newAppointment.setCoachId(json.findPath("userId").textValue());
            newAppointment.setStudentId(null);
            newAppointment.setAppointmentType("");

            Calendar startWeeklyDate = Calendar.getInstance();
            Calendar endWeeklyDate = Calendar.getInstance();
            startWeeklyDate.setTime(currentStart.getTime());
            endWeeklyDate.setTime(currentEnd.getTime());

            newAppointment.setStartDate(startWeeklyDate.getTime());
            newAppointment.setEndDate(endWeeklyDate.getTime());
            newAppointment.setAppointmentNotes("");
            newAppointment.setPresent(false);
            newAppointment.setServiceType("");
            newAppointment.setWeekly(json.findPath("weekly").asBoolean());
            newAppointment.setWeeklyId(uniqueId);
            appointmentsDB.addOrUpdate(newAppointment);
            currentStart.add(Calendar.DAY_OF_YEAR, 7);
            currentEnd.add(Calendar.DAY_OF_YEAR, 7);
        }
    }

    @Authenticate
    public Result cancelAppointment() {
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        AppointmentsModel appointment = appointmentsDB.get(appointmentId).get();
        AppointmentsModel emailAppointment = appointment.clone();
        new Thread(() -> mailerService.sendAppointmentCancellation(emailAppointment, json.findPath("cancelNotes").asText())).start();
        appointment.clearStudentData();
        appointmentsDB.addOrUpdate(appointment);
        return ok();
    }

    @Authenticate
    public Result cancelWeeklyAppointment() {
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        AppointmentsModel appointment = appointmentsDB.get(appointmentId).get();
        List<AppointmentsModel> appointments = appointmentsDB.getByWeeklyId(appointment.getWeeklyId(), new Date(), appointment.getStudentId());
        AppointmentsModel emailAppointment = appointment.clone();
        new Thread(() -> mailerService.sendAppointmentCancellation(emailAppointment, json.findPath("cancelNotes").asText())).start();
        for (AppointmentsModel ap : appointments) {
            ap.clearStudentData();
            appointmentsDB.addOrUpdate(ap);
        }
        return ok();
    }

    @Authenticate(role = "Coach")
    public Result removeAppointment() {
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        boolean removeAll = json.findPath("removeAll").asBoolean();
        AppointmentsModel appointment = appointmentsDB.remove(appointmentId).orElseThrow(NullPointerException::new);
        if (appointment.isWeekly() && removeAll) {
            List<AppointmentsModel> appointments = appointmentsDB.getByWeeklyId(appointment.getWeeklyId());
            for (AppointmentsModel ap : appointments) {
                if (ap.getStudentId() == null) {
                    appointmentsDB.remove(ap.getAppointmentId());
                }
            }
        }
        return ok();
    }

    @Authenticate(role = "Coach")
    public Result updateCoachNotes() {
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        String coachNotes = json.findPath("coachNotes").textValue();
        AppointmentsModel appointment = appointmentsDB.get(appointmentId).orElseThrow(NullPointerException::new);
        appointment.setCoachNotes(coachNotes);
        appointmentsDB.addOrUpdate(appointment);
        return ok();
    }

    @Authenticate
    public Result openAppointments(String coachId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        List<AppointmentsModel> appointments = appointmentsDB.getOpenAppointmentsByUserAndDate(coachId, startDate, endDate);
        return ok(Json.toJson(appointments));
    }

    @Authenticate
    public Result getAppointmentById(String userId, String appointmentId) {
        Optional<UsersModel> user = userDB.get(userId);
        Optional<AppointmentsModel> appointment = appointmentsDB.get(appointmentId);
        if((!appointment.isPresent() || !user.isPresent()) || (user.get().getRole().equals("Student") && !appointment.get().getStudentId().equals(userId))) {
                return notFound();
        }
        return ok(Json.toJson(appointment.get()));
    }

    @Authenticate
    public Result availableSlotsForAppointments(String coachId, String start, String end, String service) throws ParseException {
        Date startDate = new SimpleDateFormat("MM-dd-yyyy").parse(start);
        Date endDate = new SimpleDateFormat("MM-dd-yyyy").parse(end);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        calEnd.set(Calendar.HOUR_OF_DAY, 24);
        endDate = calEnd.getTime();
        List<AppointmentsModel> appointments = new ArrayList<>();
        if (coachId.equals("any")) {
            List<UsersModel> coaches = userDB.getCoachesByService(service);
            for (UsersModel coach : coaches) {
                appointments.addAll(appointmentsDB.getOpenAppointmentsByUserAndDate(coach.getUid(), startDate, endDate));
            }
        } else {
            appointments = appointmentsDB.getOpenAppointmentsByUserAndDate(coachId, startDate, endDate);
        }
        for (AppointmentsModel appointment : appointments) {
            if ( appointment.isWeekly() ) {
                List<AppointmentsModel> weeklyAppointments = appointmentsDB.getByWeeklyId(appointment.getWeeklyId(), appointment.getStartDate());
                for (AppointmentsModel weekly : weeklyAppointments) {
                    if ( weekly.getStudentId() != null ) {
                        appointment.setWeekly(false);
                        break;
                    }
                }
            }
        }
        return ok(Json.toJson(appointments));
    }


    @Authenticate
    public Result appointmentsForUser(String role, String userId, String start, String end) throws ParseException {
        Date startDate = new SimpleDateFormat("MM-dd-yyyy").parse(start);
        Date endDate = new SimpleDateFormat("MM-dd-yyyy").parse(end);
        Calendar endb = Calendar.getInstance();
        endb.setTime(endDate);
        endb.set(Calendar.HOUR_OF_DAY, 24);
        endDate = endb.getTime();
        List<AppointmentsModel> appointments;
        if ("Coach".equals(role) || "Admin".equals(role)) {
            appointments = appointmentsDB.getAppointmentsAsCoach(userId, startDate, endDate);
            appointments.addAll(appointmentsDB.getAppointmentsAsStudent(userId, startDate, endDate));
        } else {
            appointments = appointmentsDB.getAppointmentsAsStudent(userId, startDate, endDate);
        }
        return ok(Json.toJson(appointments));
    }

    @Authenticate
    public Result appointmentsAsCoach(String userId, String start, String end) throws ParseException {
        Date startDate = new SimpleDateFormat("MM-dd-yyyy").parse(start);
        Date endDate = new SimpleDateFormat("MM-dd-yyyy").parse(end);
        Calendar endc = Calendar.getInstance();
        endc.setTime(endDate);
        endc.set(Calendar.HOUR_OF_DAY, 24);
        endDate = endc.getTime();
        List<AppointmentsModel> appointments = appointmentsDB.getCoachAvailablitiliy(userId, startDate, endDate);
        return ok(Json.toJson(appointments));
    }

    @Authenticate(role = "Admin")
    public Result appointmentsByDate(String role, String userId, String start, String end) throws ParseException {
        Date startDate = new SimpleDateFormat("MM-dd-yyyy").parse(start);
        Date endDate = new SimpleDateFormat("MM-dd-yyyy").parse(end);
        Calendar endc = Calendar.getInstance();
        endc.setTime(endDate);
        endc.set(Calendar.HOUR_OF_DAY, 24);
        endDate = endc.getTime();
        List<AppointmentsModel> appointments = appointmentsDB.getAppointmentsByDate(startDate, endDate, false);
        return ok(Json.toJson(appointments));
    }

    @Authenticate(role = "Admin")
    public Result dailyViewerByDate(String start, String end) throws ParseException {
        Date startDate = new SimpleDateFormat("MM-dd-yyyy").parse(start);
        Date endDate = new SimpleDateFormat("MM-dd-yyyy").parse(end);
        Calendar endc = Calendar.getInstance();
        endc.setTime(endDate);
        endc.set(Calendar.HOUR_OF_DAY, 24);
        endDate = endc.getTime();
        List<AppointmentsModel> appointments = appointmentsDB.getAppointmentsByDate(startDate, endDate, true);
        return ok(Json.toJson(appointments));
    }
}
