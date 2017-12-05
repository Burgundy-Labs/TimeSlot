package controllers;

import play.api.Play;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

public class MailerService {

    public void sendEmail(String subject, String from, String toName, String toEmail, String bodyHtml) {
        Email email = new Email()
                .setSubject(subject)
                .setFrom("Project Burgundy <contact.project.burgundy@gmail.com>")
                .addTo(toName + "<"+toEmail+">")
                .setBodyHtml(bodyHtml);
        Play.current().injector().instanceOf(MailerClient.class).send(email);
    }

    public void sendAppointmentReminder(String subject, String from, String toName, String toEmail, String bodyHtml) {
        Email email = new Email()
                .setSubject(subject)
                .setFrom("Project Burgundy <contact.project.burgundy@gmail.com>")
                .addTo(toName + "<"+toEmail+">")
                .setBodyHtml(bodyHtml);
        Play.current().injector().instanceOf(MailerClient.class).send(email);
    }
}