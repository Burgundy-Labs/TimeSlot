package controllers;

import application_components.annotations.Authenticate;
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
    /* Roles used throughout TimeSlot */
    private String[] roles = { "Student" , "Coach", "Admin" };
    private UserDB userDB = new UserDB();

    public Result index() {
        String currentRole = getCurrentRole();
        /* Force redirect to Login is the user isn't signed in */
        if (currentRole == null) {
            return ok(views.html.login.render());
        }
        return ok(views.html.users.render());
    }

    public Result userPage(String userId) {
        if (userId == null) return notFound();
        UsersModel user = userDB.get(userId);
        if (user == null) return notFound();
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

    @Authenticate(role="Admin")
    public Result updateUserRole() {
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

    @Authenticate(role="Coach")
    public Result addServiceToCoach() {
        JsonNode json = request().body().asJson();
        String userId = json.get("userId").asText();
        String serviceText = json.get("serviceName").asText();
        String serviceId = json.get("serviceId").asText();
        ServiceModel service = new ServiceModel(serviceId, serviceText);
        userDB.addServiceToUser(userId, service);
        return ok();
    }

    @Authenticate(role="Coach")
    public Result removeServiceFromCoach() {
        JsonNode json = request().body().asJson();
        String userId = json.get("userId").asText();
        String serviceId = json.get("serviceId").asText();
        userDB.removeServiceFromUser(userId, serviceId);
        return ok();
    }

    @Authenticate(role="Admin")
    public Result removeUser() {
        try {
            JsonNode json = request().body().asJson();
            String userId = json.get("userId").asText();
            if (userDB.remove(userId) != null) {
                return ok();
            } else {
                return internalServerError();
            }
        } catch (Exception e) {
            Logger.debug(e.getMessage());
            return internalServerError();
        }
    }

    public Result getCoachesByService(String serviceId) {
        List<UsersModel> coaches = userDB.getCoachesByService(serviceId);
        coaches.sort(Comparator.comparing(UsersModel::getDisplayName));
        return ok(Json.toJson(coaches));
    }

    public UsersModel getCurrentUser() {
        String s = session("currentUser");
        if (s == null || s.isEmpty()) {
            UsersModel u = new UsersModel();
            u.setRole("Student");
            return u;
        }
        return new UserDB().get(s);
    }

    public void addAttributes(UsersModel user, String... attributes) {
        String[] o = user.getAttributes();
        String[] n = new String[o.length + attributes.length];
        System.arraycopy(o, 0, n, 0, o.length);
        System.arraycopy(attributes, 0, n, attributes.length, attributes.length);
        user.setAttributes(n);
    }

    public void removeAttribute(UsersModel user, String attribute) {
        String[] o = user.getAttributes();
        for (int i = 0; i < o.length; i++) {
            if (o[i].equalsIgnoreCase(attribute)) {
                String[] n = new String[o.length - 1];
                System.arraycopy(o, 0, n, 0, i);
                System.arraycopy(o, i + 1, n, i, o.length - i - 1);
                user.setAttributes(n);
            }
        }
    }

    public boolean hasAttribute(UsersModel user, String attribute) {
        String[] attributes = user.getAttributes();
        boolean found = false;
        for (String s : attributes) {
            if (s.equalsIgnoreCase(attribute)) {
                found = true;
            }
        }
        return found;
    }

    public String getCurrentRole() {
        return session("currentRole");
    }

    public String[] getRoles() {
        return roles;
    }
}
