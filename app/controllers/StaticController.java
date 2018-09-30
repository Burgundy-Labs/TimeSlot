package controllers;

import play.cache.Cached;
import play.mvc.Controller;
import play.mvc.Result;

public class StaticController extends BaseController {
    @Cached(key="help")
    public Result help() {
        return ok(views.html.pages.help.render());
    }
    @Cached(key="terms")
    public Result terms() {
        return ok(views.html.pages.terms.render());
    }
}