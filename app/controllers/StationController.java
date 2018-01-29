package controllers;

import play.mvc.Result;

import static play.mvc.Results.unauthorized;

public class StationController {
    public Result index() {
       // return ok(views.html.station.render());
        return unauthorized();
    }

}
