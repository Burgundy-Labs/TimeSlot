package controllers;

import controllers.ApplicationComponents.Roles;
import controllers.Databases.UserDB;
import models.UsersModel;
import play.libs.Json;
import play.mvc.Result;

import static play.mvc.Results.ok;

public class StationController {
    public Result index() {
        return ok(views.html.station.render());
    }

    public Result getUserByID(String ID){
        UsersModel u = UserDB.getUserByID(ID);
        if(u == null){
            u = new UsersModel();
            u.setRole(Roles.getRole("Student"));
            u.setID("");
        }
        return ok(Json.toJson(u));
    }
}
