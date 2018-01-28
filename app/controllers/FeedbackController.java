package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationComponents.MailerService;
import play.mvc.Controller;
import play.mvc.Result;

public class FeedbackController extends Controller {
    public Result feedback() {
        return ok(views.html.feedback.render());
    }

    public Result sendFeedback() {
        JsonNode json = request().body().asJson();
        String name = json.findPath("name").asText();
        String email = json.findPath("email").asText();
        String feedback = json.findPath("feedback").asText();
        MailerService.sendEmail(
                "New Feedback From " + name,
                name,
                email,
                "Project Burgundy",
                "contact.project.burgundy@gmail.com",
                "<p style='font-size:48px;color:#17C671;text-align:center;'>&#9997;</p><h1 style='text-align:center;'>New feedback from " + name + "!</h1> " +
                        "<h3>Name:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"
                        + name +
                        "</div></br>" +
                        "<h3>Email:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"
                        + email +
                        "</div></br>" +
                        "<h3>Feedback:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"
                        + feedback +
                        "</div></br>");
        return ok();
    }
}