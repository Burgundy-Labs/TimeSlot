package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Databases.AppointmentsDB;
import controllers.Databases.AvailabilityDB;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AvailabilityController extends Controller {

    public Result createAvailability() {
        String userRole = UserController.getCurrentRole();
        if (userRole == null || userRole.equals("Student")) {
            return forbidden(views.html.error_pages.unauthorized.render());
        }
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

    public Result availableSlotsForAppointments(String userId, String start, String end, String serviceId) {
        Date startDate = DatatypeConverter.parseDateTime(start).getTime();
        Date endDate = DatatypeConverter.parseDateTime(end).getTime();
        List<AvailabilityModel> avails;
        if (userId.equals("any")) {
            avails = availableSlotsForAny(startDate, endDate, serviceId);
        } else {
            avails = availableSlotsForCoach(userId, startDate, endDate);
        }

        return ok(Json.toJson(avails));
    }

//    private List<AvailabilityModel> availableSlotsForAny(Date startDate, Date endDate, String serviceId) {
//        List<UsersModel> coaches = UserDB.getCoachesByService(serviceId);                                 // Gets all coaches that have availability for the serviceId
//        List<AvailabilityModel> availabilities = new ArrayList<>();                                       // Creates a blank list for availabilities for availabilities of all the coaches
//        for (UsersModel coach : coaches) {                                                              // For all the coaches who have availabilities for the severiceId
//            availabilities.addAll(availableSlotsForCoach(coach.getUid(), startDate, endDate));            // Add all of the availabilities during the current week from the coach
//        }
//        List<AvailabilityModel> newAvailabilites = new ArrayList<>();                                     // Creates a new list of availabilities
//        HashMap<String, AvailabilityModel> avails = new HashMap<>();
//        for (AvailabilityModel availability : availabilities) {
//            AvailabilityModel newAv = new AvailabilityModel(null,
//                    "any",
//                    availability.getStartDate(),
//                    availability.getEndDate(),
//                    false);
//            String key = availability.getStartDate().toString() + availability.getEndDate().toString();
//            AvailabilityModel currentAv = availability;
//            if (avails.containsKey(key)) {
//                if (currentAv.getCanBeOneTime() && !currentAv.getCanBeWeekly()) {                   // If the availability can be one time and not weekly
//                    avails.get(key).addOneTimeUser(currentAv.getUserid());                                      // Add the user id into the array of one time users
//                } else { // Otherwise
//                    avails.get(key).addWeeklyUser(currentAv.getUserid());                                       // Add the user id into the array of weekly users
//                    avails.get(key).addOneTimeUser(currentAv.getUserid());                                      // Add the user id into the array of one time users
//                }
//            } else {
//                if (!newAv.getWeeklyUsers().isEmpty()) {                                                    // If the weekly users is not empty
//                    newAv.setCanBeWeekly(true);                                                               // Set can be weekly to true
//                    newAv.setCanBeOneTime(true);                                                              // Set can be one time to true
//                } else {
//                    newAv.setCanBeOneTime(true);                                                              // Set can be one time to true
//                    newAv.setCanBeWeekly(false);                                                              // Set can be weekly to false
//                }
//                avails.put(key, newAv);
//            }
//        }
//        newAvailabilites.addAll(avails.values());
//        return newAvailabilites;
//    }



    private List<AvailabilityModel> availableSlotsForAny(Date startDate, Date endDate, String serviceId) {
        List<UsersModel> coaches = UserDB.getCoachesByService(serviceId);                                 // Gets all coaches that have availability for the serviceId
        List<AvailabilityModel> availabilities = new ArrayList<>();                                       // Creates a blank list for availabilities for availabilities of all the coaches
        for (UsersModel coach : coaches) {                                                              // For all the coaches who have availabilities for the severiceId
            availabilities.addAll(availableSlotsForCoach(coach.getUid(), startDate, endDate));            // Add all of the availabilities during the current week from the coach
        }
        List<AvailabilityModel> newAvailabilites = new ArrayList<>();                                     // Creates a new list of availabilities
        HashMap<String, AvailabilityModel> avails = new HashMap<>();
        for (int i = 0; i < availabilities.size(); i++) {

            AvailabilityModel newAv = new AvailabilityModel(null,
                    "any",
                    availabilities.get(i).getStartDate(),
                    availabilities.get(i).getEndDate(),
                    false);
            // New availability model where the ID is null, the userID is any, and has the same start and end date as the original availability at i
            for (int j = i; j < availabilities.size(); j++) {                                           // For j from i to the size of the availability list size
                AvailabilityModel currentAv = availabilities.get(j);                                      // Current Availability in the J loop
                if (currentAv.getStartDate().equals(newAv.getStartDate())) {                            // If the current availability in the J loop is at the same time as the availability in the I look that was made
                    if (currentAv.getCanBeOneTime() && !currentAv.getCanBeWeekly()) {                   // If the availability can be one time and not weekly
                        newAv.addOneTimeUser(currentAv.getUserid());                                      // Add the user id into the array of one time users
                    } else { // Otherwise
                        newAv.addWeeklyUser(currentAv.getUserid());                                       // Add the user id into the array of weekly users
                        newAv.addOneTimeUser(currentAv.getUserid());                                      // Add the user id into the array of one time users
                    }
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
            newAvailabilites.add(newAv);                                                                  // Add the new availabilities to the new list of availabilities
        }
        return newAvailabilites;                                                                          // Return the list of new availabilities
    }

    private List<AvailabilityModel> availableSlotsForCoach(String userId, Date startDate, Date endDate) {
        List<AvailabilityModel> availabilities = AvailabilityDB.getAvailabilitesForUser(userId, startDate, endDate);
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsForUser("Coach", userId);
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
        String userRole = UserController.getCurrentRole();
        if (userRole == null || userRole.equals("Student")) {
            return forbidden(views.html.error_pages.unauthorized.render());
        }
        JsonNode json = request().body().asJson();
        String availabilityId = json.findPath("availabilityId").asText();
        AvailabilityDB.removeAvailability(availabilityId);
        return ok();
    }
}
