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
    private UserDB userDB = new UserDB();
    private UserController userController = new UserController();
    private SettingsController settingsController = new SettingsController();

    public Result index() {
        String currentRole = userController.getCurrentRole();
        if (currentRole == null) {
            return unauthorized(views.html.error_pages.unauthorized.render());
        }
        UsersModel currentUser = userController.getCurrentUser();
        return ok(views.html.user.render(currentUser));
    }


    public List<ServiceModel> getAvailableServices(String userId) {
        List<ServiceModel> availableServices = settingsController.getServices();
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