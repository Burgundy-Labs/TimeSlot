package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Databases.UserDB;
import models.ServiceModel;
import models.UsersModel;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

public class AccountController extends Controller {
    public Result index() {
        return ok(views.html.account.render());
    }

    public static List<ServiceModel> getAvailableServices(String userId) {

        List<ServiceModel> availableServices = SettingsController.getServices();
        List<ServiceModel> coachServices = UserDB.getServicesForUser(userId);

        for(ServiceModel s : availableServices) {
            for(ServiceModel c : coachServices) {
                if(s.getServiceId().equals(c.getServiceId())){
                    availableServices.remove(s);
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
