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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AvailabilityController extends Controller {

    public Result createAvailability() {
        String userRole = UserController.getCurrentRole();
        if(userRole == null || userRole.equals("Student")){
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
        if ( userId.equals("any") ) {
            avails = availableSlotsForAny(startDate, endDate, serviceId);
        } else {
            avails = availableSlotsForCoach(userId, startDate, endDate);
        }

        return ok(Json.toJson(avails));
    }

    private List<AvailabilityModel> availableSlotsForAny(Date startDate, Date endDate, String serviceId) {
        List<UsersModel> coaches = UserDB.getCoachesByService(serviceId);
        List<AvailabilityModel> availabilities = new ArrayList<>();
        for ( UsersModel coach : coaches ) {
            availabilities.addAll(availableSlotsForCoach(coach.getUid(), startDate, endDate));
        }
        List<AvailabilityModel> newAvailabilites = new ArrayList<>();

        for ( int i = 0; i < availabilities.size(); i++ ) {
            AvailabilityModel newAv = new AvailabilityModel(null, "any", availabilities.get(i).getStartDate(), availabilities.get(i).getEndDate(), false);
            for ( int j = i; j < availabilities.size(); j++ ) {
                AvailabilityModel currentAv = availabilities.get(j);
                if ( currentAv.getStartDate().equals(newAv.getStartDate()) ) {
                    if ( currentAv.getCanBeOneTime() && !currentAv.getCanBeWeekly() ) {
                        newAv.addOneTimeUser(currentAv.getUserid());
                    } else {
                        newAv.addWeeklyUser(currentAv.getUserid());
                        newAv.addOneTimeUser(currentAv.getUserid());
                    }
                    availabilities.remove(j);
                    j--;
                }
            }
            i--;
            if ( !newAv.getWeeklyUsers().isEmpty() ) {
                newAv.setCanBeWeekly(true);
                newAv.setCanBeOneTime(true);
            } else {
                newAv.setCanBeOneTime(true);
                newAv.setCanBeWeekly(false);
            }
            newAvailabilites.add(newAv);
        }
        return newAvailabilites;
    }

    private List<AvailabilityModel> availableSlotsForCoach(String userId, Date startDate, Date endDate) {
        List<AvailabilityModel> availabilities;
        List<AppointmentsModel> appointments;

        availabilities = AvailabilityDB.getAvailabilitesForUser(userId, startDate, endDate);
        appointments = AppointmentsDB.getAppointmentsForUser("Coach", userId);
        makeAvailabilities(userId, availabilities);

        List<AvailabilityModel> oneAvail = new ArrayList<>();
        List<AvailabilityModel> weeklyAvail = new ArrayList<>();

        for ( AvailabilityModel av : availabilities ) {
            if ( av.getWeekly() ) {

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
        for ( AvailabilityModel av : oneAvail ) {
            av.setCanBeOneTime(true);
            av.setCanBeWeekly(false);
            for ( AppointmentsModel ap : appointments ) {
                Date availabilityStart = new DateTime(av.getStartDate()).toDate();
                Date appointmentStart = new DateTime(ap.getStartDate()).toDate();
                if ( appointmentStart.equals(availabilityStart) ) {
                    toRemove.add(av);
                }
            }
        }
        oneAvail.removeAll(toRemove);

        ArrayList<AvailabilityModel> weeklyRemove = new ArrayList<>();
        for ( AvailabilityModel av : weeklyAvail ) {
            if ( av.getWeekly() ) {
                av.setCanBeWeekly(true);
                av.setCanBeOneTime(true);
            } else {
                av.setCanBeWeekly(false);
                av.setCanBeOneTime(true);
            }
            for ( AppointmentsModel ap : appointments ) {
                Calendar appointmentCalendar = Calendar.getInstance();
                appointmentCalendar.setTime(ap.getStartDate());
                Calendar availabilityCalendar = Calendar.getInstance();
                availabilityCalendar.setTime(av.getStartDate());

                if ( av.getStartDate().getTime() == ap.getStartDate().getTime() ) {
                    weeklyRemove.add(av);
                } else if ( appointmentCalendar.get(Calendar.HOUR_OF_DAY) == availabilityCalendar.get(Calendar.HOUR_OF_DAY) &&
                        appointmentCalendar.get(Calendar.MINUTE) == availabilityCalendar.get(Calendar.MINUTE) &&
                        appointmentCalendar.get(Calendar.DAY_OF_WEEK) == availabilityCalendar.get(Calendar.DAY_OF_WEEK)) {
                    if ( (appointmentCalendar.before(availabilityCalendar) || appointmentCalendar.equals(availabilityCalendar)) && ap.isWeekly() ) {
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

        List<AvailabilityModel> avails = new ArrayList<>();
        avails.addAll(weeklyAvail);
        avails.addAll(oneAvail);
        return avails;
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
        String userRole = UserController.getCurrentRole();
        if(userRole == null || userRole.equals("Student")){
            return forbidden(views.html.error_pages.unauthorized.render());
        }
        JsonNode json = request().body().asJson();
        String availabilityId = json.findPath("availabilityId").asText();
        AvailabilityDB.removeAvailability(availabilityId);
        return ok();
    }
}
