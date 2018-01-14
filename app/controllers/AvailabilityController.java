package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Databases.AppointmentsDB;
import controllers.Databases.AvailabilityDB;
import controllers.Databases.SettingsDB;
import controllers.Databases.UserDB;
import models.AppointmentsModel;
import models.AvailabilityModel;
import models.UsersModel;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AvailabilityController extends Controller {

    public Result createAvailability() {
   /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        AvailabilityModel availability = new AvailabilityModel();
        availability.setavailabilityId(json.findPath("availabilityId").textValue());
        availability.setUserid(json.findPath("userId").textValue());
        availability.setStartDate(DatatypeConverter.parseDateTime(json.findPath("startDate").textValue()).getTime());
        availability.setEndDate(DatatypeConverter.parseDateTime(json.findPath("endDate").textValue()).getTime());
        availability.setWeekly(json.findPath("weekly").booleanValue());
        /* Check if user is in DB */
        AvailabilityDB.addAvailability(availability);
        return ok();
    }

    public Result availableSlots(String userId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        List<AvailabilityModel> availabilities = AvailabilityDB.getAvailabilitesForUser(userId, startDate, endDate);
        return ok(Json.toJson(availabilities));
    }

    public Result availableSlotsForAppointments(String userId, String start, String end) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        List<AvailabilityModel> availabilities = new ArrayList<>();
        List<AppointmentsModel> appointments = new ArrayList<>();
        if (userId.equals("any")) {
            List<UsersModel> users = UserDB.getCoaches();
            for (UsersModel u : users) {
                availabilities = AvailabilityDB.getAvailabilitesForUser(u.getUid(), startDate, endDate);
                appointments = AppointmentsDB.getAppointmentsForUser("Coach", u.getUid());
                makeAvailabilities(userId, availabilities);
            }

        } else {
            availabilities = AvailabilityDB.getAvailabilitesForUser(userId, startDate, endDate);
            appointments = AppointmentsDB.getAppointmentsForUser("Coach", userId);
            makeAvailabilities(userId, availabilities);
        }
        /* Remove all taken availabilities */
        ArrayList<AvailabilityModel> toRemove = new ArrayList<>();
        for (AvailabilityModel av : availabilities) {
            for (AppointmentsModel ap : appointments) {
                Date availabilityStart = new DateTime(av.getStartDate()).toDate();
                Date availabilityEnd = new DateTime(av.getEndDate()).toDate();
                Date appointmentStart = new DateTime(ap.getStartDate()).toDate();
                Date appointmentEnd = new DateTime(ap.getEndDate()).toDate();
                if ((appointmentStart.before(availabilityStart) || appointmentStart.equals(availabilityStart)) && (appointmentEnd.after(availabilityEnd) || appointmentEnd.equals(availabilityEnd))) {
                    toRemove.add(av);
                }

                Calendar appointmentDate = Calendar.getInstance();
                appointmentDate.setTime(ap.getStartDate());
                Calendar availabilityDate = Calendar.getInstance();
                availabilityDate.setTime(av.getStartDate());

                if (appointmentStart.before(availabilityStart) || appointmentStart.equals(availabilityStart)) {
                    if (appointmentDate.get(Calendar.DAY_OF_WEEK) == availabilityDate.get(Calendar.DAY_OF_WEEK)
                            && appointmentDate.get(Calendar.HOUR) == availabilityDate.get(Calendar.HOUR)
                            && appointmentDate.get(Calendar.MINUTE) == availabilityDate.get(Calendar.MINUTE) && ap.isWeekly()) {
                        toRemove.add(av);
                    }
                }
            }
        }
        availabilities.removeAll(toRemove);

        /* Apply attributes for available slots */
        for (AvailabilityModel av : availabilities) {
            /* If the availability slot is one-time - prevent weekly appointments */
            if (!av.getWeekly()) {
                av.setCanBeWeekly(false);
                av.setCanBeOneTime(true);
            } else {
                av.setCanBeOneTime(true);
                av.setCanBeWeekly(true);
            }
            /* If any appointments exist after the availability - block weekly appointments */
            for (AppointmentsModel ap : appointments) {
                Calendar appointmentDate = Calendar.getInstance();
                appointmentDate.setTime(ap.getStartDate());
                Calendar availabilityDate = Calendar.getInstance();
                availabilityDate.setTime(av.getStartDate());
                if (ap.getStartDate().equals(av.getStartDate()) || ap.getStartDate().after(av.getStartDate())) {
                    if (appointmentDate.get(Calendar.DAY_OF_WEEK) == availabilityDate.get(Calendar.DAY_OF_WEEK)
                            && appointmentDate.get(Calendar.HOUR) == availabilityDate.get(Calendar.HOUR)
                            && appointmentDate.get(Calendar.MINUTE) == availabilityDate.get(Calendar.MINUTE)) {
                        av.setCanBeWeekly(false);
                        av.setCanBeOneTime(true);
                    }
                }
            }
        }

        return ok(Json.toJson(availabilities));
    }

    private void makeAvailabilities(String userId, List<AvailabilityModel> availabilities) {
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
    }

    public Result removeAvailability() {
        JsonNode json = request().body().asJson();
        String availabilityId = json.findPath("availabilityId").asText();
        AvailabilityDB.removeAvailability(availabilityId);
        return ok();
    }
}
