package controllers;

import models.UsersModel;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoachesController extends Controller {
    public Result index() {
        UsersModel testUser = new UsersModel();
        testUser.setEmail("test" + Math.random()+ "@test.com");
        testUser.setUserId((""+ (int)(Math.random() * 10)));
        testUser.setUsername("TESTER TEST");
        ArrayList<String> roles = new ArrayList<>();
        roles.add("admin");
        roles.add("coach");
        testUser.setRoles(roles);

        new UserDB().addUser(testUser);
        return ok(views.html.coaches.render());
    }
}
