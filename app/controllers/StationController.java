package controllers;

import play.mvc.Result;

import static play.mvc.Results.forbidden;
import static play.mvc.Results.unauthorized;

public class StationController {
    public Result index() {
       // return ok(views.html.station.render());
        return forbidden(views.html.error_pages.unauthorized.render());
    }

}
