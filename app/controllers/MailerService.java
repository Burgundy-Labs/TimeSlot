package controllers;

import models.AppointmentsModel;
import play.api.Play;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

public class MailerService {

    public static void sendEmail(String subject, String from, String toName, String toEmail, String bodyHtml) {
        Email email = new Email()
                .setSubject(subject)
                .setFrom("Project Burgundy <contact.project.burgundy@gmail.com>")
                .addTo(toName + "<"+toEmail+">")
                .setBodyHtml(bodyHtml);
        Play.current().injector().instanceOf(MailerClient.class).send(email);
    }

    public static void sendAppointmentReminder(AppointmentsModel appointment) {
        /* Coach Email */
        Email email = new Email()
                .setSubject("New Appointment With " + appointment.getStudentName() )
                .setFrom("Project Burgundy <contact.project.burgundy@gmail.com>")
                .addTo(appointment.getCoachName() + "<"+appointment.getCoachEmail()+">")
                .setBodyHtml("<h1>You have a new Appointment!</h1> <br/> <h3>Details:</h3>" +
                        "<br/>" + "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"+
                        appointment.toHTMLString() +
                        "</div>" +
                        "<br/>" +
                        "<a style=\"background-color:#0067F4; color: white; border-radius: 4px; padding: 10px; margin:0 auto; display:block; width: 90%; text-align: center; font-size: 18px; text-decoration:none; \" href=\"" +
                        "https://www.google.com/calendar/render?action=TEMPLATE" +
                        "&text=" + SettingsController.getSettings().getCenterName().replaceAll(" ", "+") + "+Appointment+With+" + appointment.getStudentName().replaceAll(" ", "+")+
                        "&dates=" +appointment.getStartDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+"/"+appointment.getEndDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+
                        "&details=" + appointment.toString().replace("\n", "%0A").replace(" ","+") +
                        "&location=" + SettingsController.getSettings().getCenterName().replaceAll(" ","+") +
                        "&sf=true" +
                        "&output=xml\"" +
                        "target=\"_blank\" rel=\"nofollow\">Add to my calendar</a> <br/>");
        Play.current().injector().instanceOf(MailerClient.class).send(email);
        /* Student Email */
        email = new Email()
                .setSubject("New Appointment With " + appointment.getCoachName() )
                .setFrom("Project Burgundy <contact.project.burgundy@gmail.com>")
                .addTo(appointment.getStudentName() + "<"+appointment.getStudentEmail()+">")
                .setBodyHtml("<h1>You have a new Appointment!</h1> <br/> <h3>Details:</h3>" +
                        "<br/>" + "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"+
                        appointment.toHTMLString() +
                        "</div>" +
                        "<br/>" +
                        "<a style=\"background-color:#0067F4; color: white; border-radius: 4px; padding: 10px; margin:0 auto; display:block; width: 90%; text-align: center; font-size: 18px; text-decoration:none; \" href=\"" +
                        "https://www.google.com/calendar/render?action=TEMPLATE" +
                        "&text=" + SettingsController.getSettings().getCenterName().replaceAll(" ", "+") + "+Appointment+With+" + appointment.getCoachName().replaceAll(" ", "+")+
                        "&dates=" +appointment.getStartDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+"/"+appointment.getEndDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+
                        "&details=" + appointment.toString().replace("\n", "%0A").replace(" ","+") +
                        "&location=" + SettingsController.getSettings().getCenterName().replaceAll(" ","+") +
                        "&sf=true" +
                        "&output=xml\"" +
                        "target=\"_blank\" rel=\"nofollow\">Add to my calendar</a> <br/>");
        Play.current().injector().instanceOf(MailerClient.class).send(email);
    }
}