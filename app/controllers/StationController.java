package controllers;

import models.UsersModel;
import play.cache.Cached;
import play.libs.Json;
import play.mvc.Result;

public class StationController extends BaseController {

    @Cached(key="station")
    public Result index() {
        return ok(views.html.pages.station.render());
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
