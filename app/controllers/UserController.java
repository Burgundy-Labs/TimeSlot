package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import databases.UserDB;
import models.ServiceModel;
import models.UsersModel;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Comparator;
import java.util.List;

public class UserController extends Controller {
    private UserDB userDB = new UserDB();
    public Result index() {
        String currentRole = UserController.getCurrentRole();
        /* Force redirect to Login is the user isn't signed in */
        if(currentRole == null) {
            return ok(views.html.login.render());
        }
        return ok(views.html.users.render());
    }

    public Result userPage(String userId){
        if(userId == null) return notFound();
        UsersModel user = userDB.get(userId);
        if(user == null) return notFound();
        return ok(views.html.user.render(user));
    }

    public Result updateUser() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        UsersModel user = Json.fromJson(json, UsersModel.class);
        /* Check if user is in DB */
        userDB.addOrUpdate(user);
        return ok();
    }

    public Result updateUserRole() {
        if(!getCurrentRole().equals("Admin")){
            return forbidden(views.html.error_pages.unauthorized.render());
        }
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        String userId = json.get("uid").asText();
        String role = json.get("role").asText();
        /* Check if user is in DB */
        UsersModel u = userDB.get(userId);
        u.setRole(role);
        userDB.addOrUpdate(u);
        return ok();
    }

    public Result addServiceToCoach() {
        String currentRole = getCurrentRole();
        if(currentRole.equals("Student")){
            return forbidden(views.html.error_pages.unauthorized.render());
        }
        JsonNode json = request().body().asJson();
        String userId = json.get("userId").asText();
        String serviceText = json.get("serviceName").asText();
        String serviceId = json.get("serviceId").asText();
        ServiceModel service = new ServiceModel(serviceId, serviceText);
        userDB.addServiceToUser(userId, service);
        return ok();
    }

    public Result removeServiceFromCoach() {
        JsonNode json = request().body().asJson();
        String userId = json.get("userId").asText();
        String serviceId = json.get("serviceId").asText();
        userDB.removeServiceFromUser(userId, serviceId);
        return ok();
    }

    public Result removeUser() {
        if(!getCurrentRole().equals("Admin")){
            return forbidden(views.html.error_pages.unauthorized.render());
        }
        try {
            JsonNode json = request().body().asJson();
            String userId = json.get("userId").asText();
            if(userDB.delete(userId) != null){
                return ok();
            } else {
                return internalServerError();
            }
       } catch(Exception e) {
            Logger.debug(e.getMessage());
            return internalServerError();
        }
    }

    public Result getCoachesByService(String serviceId) {
        List<UsersModel> coaches = userDB.getCoachesByService(serviceId);
        coaches.sort(Comparator.comparing(UsersModel::getDisplayName));
        return ok(Json.toJson(coaches));
    }

    public static UsersModel getCurrentUser() {
        String s = session("currentUser");
        if(s == null || s.isEmpty()) {
            UsersModel u = new UsersModel();
            u.setRole("Student");
            return u;
    }
        return new UserDB().get(s);
    }

    public static String getCurrentRole(){
        return session("currentRole");
    }
}
