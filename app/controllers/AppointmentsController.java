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
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AppointmentsController extends Controller {
    private AppointmentsDB appointmentsDB = new AppointmentsDB();
    private SettingsDB settingsDB = new SettingsDB();
    private MailerService mailerService = new MailerService();
    private UserDB userDB = new UserDB();

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
        JsonNode json = request().body().asJson();
        AppointmentsModel availability = appointmentsDB.get(json.findPath("appointmentId").asText()).get();
        Date startDate = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue()).getTime();
        Date endDate = DatatypeConverter.parseDateTime(json.findPath("endDate").textValue()).getTime();
        UsersModel student = userDB.get(json.findPath("studentId").asText()).get();
        AppointmentsModel appointment = availability.clone();
        appointment.setServiceType(json.findPath("serviceType").asText());
        appointment.setAppointmentType(json.findPath("appointmentType").asText());
        appointment.setDescription(null);
        appointment.setPresent(false);
        appointment.setStartDate(startDate);
        appointment.setEndDate(endDate);
        if ( !json.findPath("weekly").asBoolean() ) {
            appointment.setWeeklyId(null);
            appointment.setWeekly(false);
        }
        // Send email
        AppointmentsModel emailAppointment = appointment.clone();
        emailAppointment.setStudentData(student.getUid(), student.getEmail(), student.getDisplayName(), student.getPhotoURL());
        new Thread(() -> mailerService.sendAppointmentConfirmation(emailAppointment)).start();


        if ( availability.isWeekly() ) {
            String UUID1 = UUID.randomUUID().toString();
            String UUID2 = UUID.randomUUID().toString();
            String UUID3 = UUID.randomUUID().toString();
            Calendar startAppointment = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue());
            Calendar endAppointment = DatatypeConverter.parseDateTime(json.findPath("endDate").textValue());
            Calendar start = DatatypeConverter.parseDateTime(json.findPath("startDate").textValue());
            Calendar end = DatatypeConverter.parseDateTime(json.findPath("endDate").textValue());

            List<AppointmentsModel> weeklyAvailabilities = appointmentsDB.getByWeeklyId(availability.getWeeklyId());
            Calendar startWeekly = Calendar.getInstance();
            startWeekly.setTime(weeklyAvailabilities.get(0).getStartDate());
            while ( startWeekly.get(Calendar.WEEK_OF_YEAR) != startAppointment.get(Calendar.WEEK_OF_YEAR) ) {
                startAppointment.add(Calendar.DAY_OF_YEAR, -7);
                endAppointment.add(Calendar.DAY_OF_YEAR, -7);
            }
            for ( AppointmentsModel weeklyAvailability : weeklyAvailabilities ) {
                startAppointment.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
                startAppointment.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
                endAppointment.set(Calendar.HOUR_OF_DAY, end.get(Calendar.HOUR_OF_DAY));
                endAppointment.set(Calendar.MINUTE, end.get(Calendar.MINUTE));
                appointment.setStartDate(startAppointment.getTime());
                appointment.setEndDate(endAppointment.getTime());
                if ( appointment.isWeekly() ) {
                    if ( appointment.getStartDate().after(startDate) || appointment.getStartDate().equals(startDate)) {
                        split(weeklyAvailability, student, appointment.clone(), UUID1, UUID2, UUID3, true);
                    } else {
                        split(weeklyAvailability, student, appointment.clone(), UUID1, UUID2, UUID3, false);
                    }
                } else {
                    if ( appointment.getStartDate().equals(startDate) ) {
                        split(weeklyAvailability, student, appointment.clone(), UUID1, UUID2, UUID3, true);
                    } else {
                        split(weeklyAvailability, student, appointment.clone(), UUID1, UUID2, UUID3, false);
                    }
                }
                startAppointment.add(Calendar.DAY_OF_YEAR, 7);
                endAppointment.add(Calendar. DAY_OF_YEAR, 7);
            }
        } else {
            split(availability, student, appointment);
        }
        return ok();
    }

    private AppointmentsModel split(AppointmentsModel availability, UsersModel student, AppointmentsModel appointment) {
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
        return appointment;
    }

    private AppointmentsModel split(AppointmentsModel availability, UsersModel student, AppointmentsModel appointment, String UUID1, String UUID2, String UUID3, boolean isAppointment) {
        if (availability.getStartDate().before(appointment.getStartDate()) && availability.getEndDate().equals(appointment.getEndDate())) {
            availability.setEndDate(appointment.getStartDate());
            availability.setWeeklyId(UUID3);
            appointment.setAppointmentId(null);
            appointment.setWeeklyId(UUID1);
            availability.setWeekly(true);
            appointmentsDB.addOrUpdate(availability);
        } else if (availability.getStartDate().equals(appointment.getStartDate()) && availability.getEndDate().after(appointment.getEndDate())) {
            availability.setStartDate(appointment.getEndDate());
            availability.setWeeklyId(UUID3);
            appointment.setAppointmentId(null);
            availability.setWeekly(true);
            appointment.setWeeklyId(UUID1);
            appointmentsDB.addOrUpdate(availability);
        } else if (availability.getStartDate().before(appointment.getStartDate()) && availability.getEndDate().after(appointment.getEndDate())) {
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
            }
            appointmentsDB.addOrUpdate(availability);
            return availability;
        }
        if (isAppointment) {
            appointment.setStudentData(student.getUid(), student.getEmail(), student.getDisplayName(), student.getPhotoURL());
        } else {
            appointment.clearStudentData();
        }
        appointmentsDB.addOrUpdate(appointment);
        return appointment;
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
        Boolean weekly = json.findPath("weekly").asBoolean();
        appointment.setWeekly(weekly);
        if (appointment.isWeekly()) {
            appointment.setWeeklyId(uniqueId);
            new Thread(() -> createWeeklyAvailabilities(json, uniqueId)).start();
        } else {
            appointment.setWeeklyId("");
        }
        appointmentsDB.addOrUpdate(appointment);
        /* Check if user is in DB */

        // new Thread(() -> mailerService.sendAppointmentConfirmation(appointment)).start();

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

    public Result cancelWeeklyAppointment() {
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        AppointmentsModel appointment = appointmentsDB.get(appointmentId).get();
        List<AppointmentsModel> appointments = appointmentsDB.getByWeeklyId(appointment.getWeeklyId(), appointment.getStartDate(), appointment.getStudentId());
        AppointmentsModel emailAppointment = appointment.clone();
        new Thread(() -> mailerService.sendAppointmentCancellation(emailAppointment, json.findPath("cancelNotes").asText())).start();
        appointment.clearStudentData();
        appointmentsDB.addOrUpdate(appointment);
        for ( AppointmentsModel ap : appointments  ) {
            ap.clearStudentData();
            appointmentsDB.addOrUpdate(ap);
        }
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

    public Result openAppointments(String coachId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        List<AppointmentsModel> appointments = appointmentsDB.getOpenAppointmentsByUserAndDate(coachId, startDate, endDate);
        return ok(Json.toJson(appointments));
    }

    public Result availableSlotsForAppointments(String coachId, String start, String end, String service) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        calEnd.set(Calendar.HOUR_OF_DAY, 24);
        endDate = calEnd.getTime();
        List<AppointmentsModel> appointments = new ArrayList<>();
        if (coachId.equals("any")) {
            List<UsersModel> coaches = userDB.getCoachesByService(service);
            for ( UsersModel coach : coaches ) {
                appointments.addAll(appointmentsDB.getOpenAppointmentsByUserAndDate(coach.getUid(), startDate, endDate));
            }
        } else {
            appointments = appointmentsDB.getOpenAppointmentsByUserAndDate(coachId, startDate, endDate);
        }
        List<AvailabilityModel> availabilities = new ArrayList<>();
        for ( AppointmentsModel app : appointments ) {
            AvailabilityModel av = new AvailabilityModel(app.getAppointmentId(), app.getCoachId(), app.getStartDate(), app.getEndDate(), app.isWeekly());
            av.setCanBeWeekly(app.isWeekly());
            av.setCanBeOneTime(true);
            if ( app.isWeekly() ) {
                List<AppointmentsModel> weeklyAppointments = appointmentsDB.getByWeeklyId(app.getWeeklyId(), app.getStartDate());
                for (AppointmentsModel appointment : weeklyAppointments){
                    if ( (appointment.getStudentId() != null) && appointment.getStartDate().after(app.getStartDate()) ) {
                        av.setCanBeWeekly(false);
                        break;
                    }
                }
            }
            long duration = av.getEndDate().getTime() - av.getStartDate().getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            if (diffInMinutes / 30 != 1 && diffInMinutes != 0) {
                for (int i = 0; i < diffInMinutes / 30; i++) {
                    AvailabilityModel newAvail = new AvailabilityModel
                            (av.getavailabilityId(),
                                    app.getCoachId(),
                                    DateUtils.addMinutes(av.getStartDate(), 30 * i),
                                    DateUtils.addMinutes(av.getStartDate(), 30 * (i + 1)),
                                    av.getWeekly()
                            );
                    newAvail.setCanBeWeekly(av.getCanBeWeekly());
                    newAvail.setCanBeOneTime(av.getCanBeOneTime());
                    availabilities.add(newAvail);
                }
            } else {
                availabilities.add(av);
            }
        }

        return ok(Json.toJson(availableSlotsForAny(availabilities)));
    }

    private List<AvailabilityModel> availableSlotsForAny(List<AvailabilityModel> availabilities) {

        List<AvailabilityModel> newAvailabilities = new ArrayList<>();                                     // Creates a new list of availabilities
        for (int i = 0; i < availabilities.size(); i++) {
            AvailabilityModel newAv = new AvailabilityModel(availabilities.get(i).getavailabilityId(),
                    "any",
                    availabilities.get(i).getStartDate(),
                    availabilities.get(i).getEndDate(),
                    false);
            // New availability model where the ID is null, the userID is any, and has the same start and end date as the original availability at i
            for (int j = i; j < availabilities.size(); j++) {                                           // For j from i to the size of the availability list size
                AvailabilityModel currentAv = availabilities.get(j);                                      // Current Availability in the J loop
                if (currentAv.getStartDate().equals(newAv.getStartDate())) {                            // If the current availability in the J loop is at the same time as the availability in the I look that was made
                    if (currentAv.getCanBeWeekly()) {                   // If the availability can be one time and not weekly
                        newAv.addWeeklyUser(currentAv.getavailabilityId());                                       // Add the user id into the array of weekly users
                    }
                    newAv.addOneTimeUser(currentAv.getavailabilityId());
                    availabilities.remove(j);                                                             // Remove the availability from the list of availabilities
                    j--;                                                                                  // Decrease j by one so that the loop goes to the next one
                }
            }
            i--;                                                                                          // Decrease i by one so that the loop goes to the next one
            if (!newAv.getWeeklyUsers().isEmpty()) {                                                    // If the weekly users is not empty
                newAv.setCanBeWeekly(true);                                                               // Set can be weekly to true
                newAv.setCanBeOneTime(true);                                                              // Set can be one time to true
            } else {
                newAv.setCanBeOneTime(true);                                                              // Set can be one time to true
                newAv.setCanBeWeekly(false);                                                              // Set can be weekly to false
            }
            newAvailabilities.add(newAv);                                                                  // Add the new availabilities to the new list of availabilities
        }
        return newAvailabilities;                                                                          // Return the list of new availabilities
    }

    public Result appointmentsForUser(String role, String userId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        Calendar endb = Calendar.getInstance();
        endb.setTime(endDate);
        endb.set(Calendar.HOUR_OF_DAY, 24);
        endDate = endb.getTime();
        List<AppointmentsModel> appointments;
        if ( "Coach".equals(role) || "Admin".equals(role) ) {
            appointments = appointmentsDB.getAppointmentsAsCoach(userId, startDate, endDate);
            appointments.addAll(appointmentsDB.getAppointmentsAsStudent(userId, startDate, endDate));
        } else {
            appointments = appointmentsDB.getAppointmentsAsStudent(userId, startDate, endDate);
        }
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
