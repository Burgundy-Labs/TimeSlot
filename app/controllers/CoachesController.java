package controllers;

import models.UsersModel;
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
}
