package controllers;

import application_components.annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import databases.AppointmentsDB;
import databases.AvailabilityDB;
import databases.UserDB;
import models.AppointmentsModel;
import models.AvailabilityModel;
import models.UsersModel;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/* TODO remove AvailabilityDB and replicate functionality with nulled appointment values  */
public class AvailabilityController extends Controller {
    private AppointmentsDB appointmentsDB = new AppointmentsDB();
    private AvailabilityDB availabilityDB = new AvailabilityDB();
    private UserDB userDB = new UserDB();
    private UserController userController = new UserController();


    /* Will now create an appointment with no student attached */
    @Authenticate(role="Coach")
    public Result createAvailability() {
     /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        AppointmentsModel availability = new AppointmentsModel();
        availability.setAppointmentId(json.findPath("appointmentID").textValue());
        availability.setCoachId(json.findPath("userId").textValue());
        availability.setStartDate(DatatypeConverter.parseDateTime(json.findPath("startDate").textValue()).getTime());
        availability.setEndDate(DatatypeConverter.parseDateTime(json.findPath("endDate").textValue()).getTime());
        // TODO add logic to create weekly empty appointments (look at old avail / weekly appmnt code)
        // TODO Potentially add a "create weekly appointment" method in DB for easier use
        availability.setWeekly(json.findPath("weekly").booleanValue());
        /* Check if user is in DB */
        appointmentsDB.addOrUpdate(availability);
        return ok();
    }

    public Result availableSlots(String userId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        List<AvailabilityModel> availabilities = availabilityDB.getAvailabilitesForUser(userId, startDate, endDate);
        return ok(Json.toJson(availabilities));
    }

    public Result availableSlotsForAppointments(String userId, String start, String end, String serviceId) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        List<AvailabilityModel> avails;
        if ("any".equals(userId)) {
            avails = new ArrayList<>(); //availableSlotsForAny(startDate, endDate, serviceId);
        } else {
            avails = availableSlotsForCoach(userId, startDate, endDate);
        }

        return ok(Json.toJson(avails));
    }



    private List<AvailabilityModel> availableSlotsForCoach(String userId, Date startDate, Date endDate) {
        List<AvailabilityModel> availabilities = availabilityDB.getAvailabilitesForUser(userId, startDate, endDate);
        List<AppointmentsModel> appointments = appointmentsDB.getAppointmentsForUser("Coach", userId);
        availabilities = makeAvailabilities(userId, availabilities);
        List<AvailabilityModel> oneAvail = new ArrayList<>();
        List<AvailabilityModel> weeklyAvail = new ArrayList<>();
        for (AvailabilityModel av : availabilities) {
            if (av.getWeekly()) {
                Calendar availabilityDate = Calendar.getInstance();
                availabilityDate.setTime(av.getStartDate());
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(startDate);
                Calendar newAvailability = Calendar.getInstance();
                newAvailability.set(Calendar.HOUR_OF_DAY, availabilityDate.get(Calendar.HOUR_OF_DAY));
                newAvailability.set(Calendar.MINUTE, availabilityDate.get(Calendar.MINUTE));
                newAvailability.set(Calendar.SECOND, 0);
                newAvailability.set(Calendar.MILLISECOND, 0);
                newAvailability.set(Calendar.DAY_OF_YEAR, startCalendar.get(Calendar.DAY_OF_YEAR) + availabilityDate.get(Calendar.DAY_OF_WEEK) - 2);
                Date startTime = newAvailability.getTime();
                newAvailability.add(Calendar.MINUTE, 30);
                Date endTime = newAvailability.getTime();
                weeklyAvail.add(new AvailabilityModel(
                        av.getavailabilityId(),
                        av.getUserid(),
                        startTime,
                        endTime,
                        true));
            } else {
                oneAvail.add(av);
            }
        }

        ArrayList<AvailabilityModel> toRemove = new ArrayList<>();
        for (AvailabilityModel av : oneAvail) {
            av.setCanBeOneTime(true);
            av.setCanBeWeekly(false);
            for (AppointmentsModel ap : appointments) {
                Date availabilityStart = new DateTime(av.getStartDate()).toDate();
                Date appointmentStart = new DateTime(ap.getStartDate()).toDate();
                if (appointmentStart.equals(availabilityStart)) {
                    toRemove.add(av);
                }
            }
        }
        oneAvail.removeAll(toRemove);

        ArrayList<AvailabilityModel> weeklyRemove = new ArrayList<>();
        for (AvailabilityModel av : weeklyAvail) {
            av.setCanBeWeekly(true);
            av.setCanBeOneTime(true);
            for (AppointmentsModel ap : appointments) {
                Calendar appointmentCalendar = Calendar.getInstance();
                appointmentCalendar.setTime(ap.getStartDate());
                Calendar availabilityCalendar = Calendar.getInstance();
                availabilityCalendar.setTime(av.getStartDate());
                if (av.getStartDate().getTime() == ap.getStartDate().getTime()) {
                    weeklyRemove.add(av);
                } else if (appointmentCalendar.get(Calendar.HOUR_OF_DAY) == availabilityCalendar.get(Calendar.HOUR_OF_DAY) &&
                        appointmentCalendar.get(Calendar.MINUTE) == availabilityCalendar.get(Calendar.MINUTE) &&
                        appointmentCalendar.get(Calendar.DAY_OF_WEEK) == availabilityCalendar.get(Calendar.DAY_OF_WEEK)) {
                    if ((appointmentCalendar.before(availabilityCalendar) || appointmentCalendar.equals(availabilityCalendar)) && ap.isWeekly()) {
                        weeklyRemove.add(av);
                    } else if ((appointmentCalendar.before(availabilityCalendar) || appointmentCalendar.equals(availabilityCalendar)) && !ap.isWeekly()) {
                        av.setCanBeOneTime(true);
                        av.setCanBeWeekly(true);
                    } else {
                        av.setCanBeOneTime(true);
                        av.setCanBeWeekly(false);
                    }
                }
            }
        }
        weeklyAvail.removeAll(weeklyRemove);
        List<AvailabilityModel> avails = new ArrayList<>(weeklyAvail);
        avails.addAll(oneAvail);
        return avails;
    }

    private List<AvailabilityModel> makeAvailabilities(String userId, List<AvailabilityModel> availabilities) {
        for (int j = 0; j < availabilities.size(); j++) {
            AvailabilityModel a = availabilities.get(j);
            Date startDate = new DateTime(a.getStartDate()).toDate();
            Date endDate = new DateTime(a.getEndDate()).toDate();
            long duration = endDate.getTime() - startDate.getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            if (diffInMinutes / 30 != 1) {
                for (int i = 0; i < diffInMinutes / 30; i++) {
                    availabilities.add(new AvailabilityModel
                            (a.getavailabilityId(),
                                    userId,
                                    DateUtils.addMinutes(a.getStartDate(), 30 * i),
                                    DateUtils.addMinutes(a.getStartDate(), 30 * (i + 1)),
                                    a.getWeekly()
                            ));
                }
                availabilities.remove(a);
                j--;
            }
        }
        return availabilities;
    }

    public Result removeAvailability() {
        String userRole = userController.getCurrentRole();
        if (userRole == null || userRole.equals("Student")) {
            return forbidden(views.html.pages.error_pages.unauthorized.render());
        }
        JsonNode json = request().body().asJson();
        String availabilityId = json.findPath("availabilityId").asText();
        availabilityDB.removeAvailability(availabilityId);
        return ok();
    }
}
