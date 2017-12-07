package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class FeedbackController extends Controller {
    public Result feedback() {
        return ok(views.html.feedback.render());
    }


}