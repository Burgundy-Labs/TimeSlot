package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class StaticController extends BaseController {
    public Result help() {
        return ok(views.html.pages.help.render());
    }
    public Result terms() {
        return ok(views.html.pages.terms.render());
    }
}