package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Databases.SettingsDB;
import controllers.Databases.UserDB;
import models.SettingsModel;
import models.UsersModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class SettingsController extends Controller {

    public Result index() {
        return ok(views.html.settings.render());
    }

    public Result updateSettings() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        SettingsModel settings = Json.fromJson(json, SettingsModel.class);
        /* Check if user is in DB */
        SettingsDB.changeSettings(settings);
        return ok();
    }

    public static SettingsModel getSettings() {
        return  SettingsDB.getSettings();
    }

}
