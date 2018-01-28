package controllers;

import play.mvc.Result;

import static play.mvc.Results.ok;

public class StationController {
    public Result index() {
        return ok(views.html.station.render());
    }

}
