package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Databases.AppointmentsDB;
import controllers.Databases.AvailabilityDB;
import models.AppointmentsModel;
import models.AvailabilityModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

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

    public Result removeAvailability(){
        JsonNode json = request().body().asJson();
        String availabilityId = json.findPath("availabilityId").asText();
        AvailabilityDB.removeAvailability(availabilityId);
        return ok();
    }
}
