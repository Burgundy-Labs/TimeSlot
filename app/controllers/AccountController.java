package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import databases.UserDB;
import models.ServiceModel;
import models.UsersModel;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Iterator;
import java.util.List;

public class AccountController extends Controller {
    UserDB userDB = new UserDB();

    public Result index() {
        String currentRole = UserController.getCurrentRole();
        if (currentRole == null) {
            return unauthorized(views.html.error_pages.unauthorized.render());
        }
        UsersModel currentUser = UserController.getCurrentUser();
        return ok(views.html.user.render(currentUser));
    }


    public List<ServiceModel> getAvailableServices(String userId) {
        List<ServiceModel> availableServices = SettingsController.getServices();
        List<ServiceModel> coachServices = userDB.getServicesForUser(userId);

        for (Iterator<ServiceModel> serviceIterator = availableServices.iterator(); serviceIterator.hasNext(); ) {
            ServiceModel service = serviceIterator.next();
            for (ServiceModel coach : coachServices) {
                if (service.getServiceId().equals(coach.getServiceId())) {
                    serviceIterator.remove();
                }
            }
        }
        return availableServices;
    }

    public List<ServiceModel> getServicesForUser(String userId) {
        List<ServiceModel> coachServices = userDB.getServicesForUser(userId);
        return coachServices;
    }
}