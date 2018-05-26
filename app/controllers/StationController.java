package controllers;

import databases.UserDB;
import models.UsersModel;
import play.libs.Json;
import play.mvc.Result;

import static play.mvc.Results.ok;

public class StationController {
    UserDB userDB = new UserDB();
    public Result index() {
        return ok(views.html.station.render());
    }

    public Result getUserByID(String ID){
        UsersModel u = userDB.getByAuth_ID(ID);
        if(u == null){
            u = new UsersModel();
            /* Set role as Student if no user found*/
            u.setRole("Student");
            u.setAuth_id("");
        }
        return ok(Json.toJson(u));
    }
}
