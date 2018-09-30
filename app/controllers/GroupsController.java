package controllers;

import application_components.annotations.Authenticate;
import play.cache.Cached;
import play.mvc.Result;

public class GroupsController extends BaseController {

    @Cached(key="groups")
    @Authenticate
    public Result index() {
        return ok(views.html.pages.groups.render());
    }
}
