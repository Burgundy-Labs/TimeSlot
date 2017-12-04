package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationComponents.Roles;
import controllers.Databases.UserDB;
import models.NotificationModel;
import models.UsersModel;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.management.relation.Role;

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

    public Result addNotificationToUser() {
        JsonNode json = request().body().asJson();
        String userId = json.get("userId").asText();
        NotificationModel notification = new NotificationModel();
        notification.setNotificationContent(json.get("notificationContent").asText());
        UserDB.addNotificationToUser(notification,userId);
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

    public static UsersModel getCurrentUser() {
        String s = session("currentUser");

        return UserDB.getUser(s);
    }
}
