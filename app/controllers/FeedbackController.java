package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import application_components.mailing.MailerService;
import play.mvc.Controller;
import play.mvc.Result;

public class FeedbackController extends Controller {
    private MailerService mailerService = new MailerService();

    public Result feedback() {
        return ok(views.html.pages.feedback.render());
    }

    /* TOOD use EmailBuilder for Feedback Email */
    public Result sendFeedback() {
        JsonNode json = request().body().asJson();
        String name = json.findPath("name").asText();
        String email = json.findPath("email").asText();
        String feedback = json.findPath("feedback").asText();
        mailerService.sendEmail(
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