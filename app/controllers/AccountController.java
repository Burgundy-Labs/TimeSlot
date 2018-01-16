package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Databases.UserDB;
import models.ServiceModel;
import models.UsersModel;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Iterator;
import java.util.List;

public class AccountController extends Controller {
    public Result index() {
        return ok(views.html.account.render());
    }


    public Result isCoachChecked() {
        JsonNode json = request().body().asJson();
        Boolean checked = json.findPath("checked").asBoolean();
        String userId = json.findPath("userId").asText();
        UsersModel u = UserDB.getUser(userId);
        u.setIsCoach(checked);
        UserDB.addUser(u);
        return ok();
    }

    public static List<ServiceModel> getAvailableServices(String userId) {
        List<ServiceModel> availableServices = SettingsController.getServices();
        List<ServiceModel> coachServices = UserDB.getServicesForUser(userId);

        for (Iterator<ServiceModel> serviceIterator = availableServices.iterator(); serviceIterator.hasNext();) {
            ServiceModel service = serviceIterator.next();
            for (ServiceModel coach : coachServices) {
                if (service.getServiceId().equals(coach.getServiceId())) {
                    serviceIterator.remove();
                }
            }
        }
        return availableServices;
    }

    public static List<ServiceModel> getServicesForUser(String userId) {
        List<ServiceModel> coachServices = UserDB.getServicesForUser(userId);
        return coachServices;
    }
}
