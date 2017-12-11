package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Databases.AppointmentsDB;
import controllers.Databases.AvailabilityDB;
import models.AppointmentsModel;
import models.AvailabilityModel;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.DatatypeConverter;
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
        availability.setStartDate(json.findPath("startDate").textValue());
        availability.setEndDate(json.findPath("endDate").textValue());
        availability.setWeekly(json.findPath("weekly").booleanValue());
        /* Check if user is in DB */
        AvailabilityDB.addAvailability(availability);
        return ok();
    }

    public Result availableSlots(String userId) {
        List<AvailabilityModel> availabilities = AvailabilityDB.getAvailabilitesForUser(userId);
        return ok(Json.toJson(availabilities));
    }

    public Result availableSlotsForAppointments(String userId) {
        List<AvailabilityModel> availabilities = AvailabilityDB.getAvailabilitesForUser(userId);
        List<AppointmentsModel> appointments = AppointmentsDB.getAppointmentsForUser("Coach", userId);
        int size = availabilities.size();
        for (int j = 0; j < size; j++) {
            AvailabilityModel a = availabilities.get(j);
            Date startDate = new DateTime( a.getStartDate() ).toDate();
            Date endDate = new DateTime( a.getEndDate() ).toDate();
            long duration = startDate.getTime() - endDate.getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            if (diffInMinutes / 30 != 1) {
                for (int i = 0; i < diffInMinutes / 30; i++) {
                    availabilities.add(new AvailabilityModel
                            (a.getavailabilityId(),
                                    userId,
                                    DateUtils.addMinutes(DatatypeConverter.parseDateTime(a.getStartDate()).getTime(), 30 * i).toInstant().toString().replaceAll("-", "").replaceAll(":", ""),
                                    DateUtils.addMinutes(DatatypeConverter.parseDateTime(a.getStartDate()).getTime(), 30 * (i + 1)).toInstant().toString().replaceAll("-", "").replaceAll(":", ""),
                                    a.getWeekly()
                            ));
                }
                availabilities.remove(a);
                j += (diffInMinutes / 30);
                size = availabilities.size();
            }
        }
//        for(AvailabilityModel av : availabilities) {
//            for(AppointmentsModel ap : appointments) {
//                Date availabilityStart = new DateTime( av.getStartDate() ).toDate();
//                Date availabilityEnd = new DateTime( av.getEndDate() ).toDate();
//                Date appointmentStart = new DateTime( ap.getStartDate() ).toDate();
//                Date appointmentEnd = new DateTime( ap.getEndDate() ).toDate();
//                if( (appointmentStart.before(availabilityStart) || appointmentStart.equals(availabilityStart)) && (appointmentEnd.after(availabilityEnd) || appointmentEnd.equals(availabilityEnd)) ) {
//                    System.out.println("Test");
//                    availabilities.remove(av);
//                    break;
//                }
//            }
//        }
        return ok(Json.toJson(availabilities));
    }

    public Result removeAvailability() {
        JsonNode json = request().body().asJson();
        String availabilityId = json.findPath("availabilityId").asText();
        AvailabilityDB.removeAvailability(availabilityId);
        return ok();
    }
}
