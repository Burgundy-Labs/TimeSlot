package controllers;

import play.mvc.Result;

import static play.mvc.Results.forbidden;

public class StationController {
    public Result index() {
       // return ok(views.html.station.render());
        return forbidden();
    }

}
