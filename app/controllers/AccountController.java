package controllers;

import controllers.Databases.UserDB;
import models.ServiceModel;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Iterator;
import java.util.List;

public class AccountController extends Controller {
    public Result index() {
        return ok(views.html.account.render());
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
