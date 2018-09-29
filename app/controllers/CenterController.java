package controllers;

import application_components.annotations.Authenticate;
import play.cache.Cached;
import play.mvc.Result;

public class CenterController extends BaseController {

    @Cached(key="center")
    @Authenticate
    public Result index() {
        return ok(views.html.pages.center.render());
    }
}
