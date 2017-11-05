package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.UsersModel;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoachesController extends Controller {
    public Result index() {

        return ok(views.html.coaches.render());
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
}
