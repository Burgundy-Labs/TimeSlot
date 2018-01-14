package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationComponents.Roles;
import controllers.Databases.UserDB;
import models.NotificationModel;
import models.ServiceModel;
import models.UsersModel;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class UserController extends Controller {
    public Result index() {
        return ok(views.html.coaches.render());
    }

    public Result updateUser() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        UsersModel user = Json.fromJson(json, UsersModel.class);
        /* Check if user is in DB */
        UserDB.addUser(user);
        return ok();
    }

    public Result updateUserRole() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        String userId = json.get("uid").asText();
        String role = json.get("role").asText();
        /* Check if user is in DB */
        UsersModel u = UserDB.getUser(userId);
        u.setRole(Roles.getRole(role));
        UserDB.addUser(u);
        return ok();
    }

    public Result addServiceToCoach() {
        JsonNode json = request().body().asJson();
        String userId = json.get("userId").asText();
        String serviceText = json.get("serviceName").asText();
        String serviceId = json.get("serviceId").asText();
        ServiceModel service = new ServiceModel(serviceId, serviceText);
        UserDB.addServiceToUser(userId, service);
        return ok();
    }

    public Result addNotificationToUser() {
        JsonNode json = request().body().asJson();
        String userId = json.get("userId").asText();
        NotificationModel notification = new NotificationModel();
        notification.setNotificationContent(json.get("notificationContent").asText());
        UserDB.addNotificationToUser(notification,userId);
        return ok();
    }

    public Result removeServiceFromCoach() {
        JsonNode json = request().body().asJson();
        String userId = json.get("userId").asText();
        String serviceId = json.get("serviceId").asText();
        UserDB.removeServiceFromUser(userId, serviceId);
        return ok();
    }

    public Result removeNotificationFromUser() {
        JsonNode json = request().body().asJson();
        String userId = json.get("userId").asText();
        String notificationId = json.get("notificationId").asText();
        UserDB.removeNotification(userId, notificationId);
        return ok();
    }

    public Result removeUser() {
        try {
            JsonNode json = request().body().asJson();
            String userId = json.get("userId").asText();
            if(UserDB.removeUser(userId)){
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
        List<UsersModel> coaches = UserDB.getCoachesByService(serviceId);
        return ok(Json.toJson(coaches));
    }

    public static UsersModel getCurrentUser() {
        String s = session("currentUser");
        if(s == null || s.isEmpty()) {
            UsersModel u = new UsersModel();
            u.setRole(Roles.getRole("Student"));
            return u;
    }
        return UserDB.getUser(s);
    }
}
