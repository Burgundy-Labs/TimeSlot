package controllers;

import application_components.annotations.Authenticate;
import play.mvc.Result;

public class GroupsController extends BaseController {

    @Authenticate
    public Result index() {
        return ok(views.html.pages.groups.render());
    }
}
