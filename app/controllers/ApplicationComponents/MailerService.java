package controllers.ApplicationComponents;

import com.google.inject.Inject;
import controllers.Application;
import controllers.SettingsController;
import models.AppointmentsModel;
import models.SettingsModel;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import java.text.DateFormat;

public class MailerService {

    private static MailerClient mailerClient;

    @Inject
    MailerService(MailerClient mailerClient){
        MailerService.mailerClient = mailerClient;
    }

    public static void sendEmail(String subject, String fromName, String fromEmail, String toName, String toEmail, String bodyHtml) {
        Email email = new Email()
                .setSubject(subject)
                .setFrom(fromName +"<"+fromEmail+">")
                .addTo(toName + "<"+toEmail+">")
                .setBodyHtml(bodyHtml);
       mailerClient.send(email);
    }

    static void sendAppointmentReminder(AppointmentsModel appointment) {
        SettingsModel settings = SettingsController.getSettings();
        /* Coach Email */
        Email email = new Email()
                .setSubject("Appointment Reminder for " + DateFormat.getDateTimeInstance().format(appointment.getStartDate()) + " at the " + settings.getCenterName() )
                .setFrom("Project Burgundy <"+ Application.getConfig().getString("play.mailer.user") +">")
                .addTo(appointment.getCoachName() + "<"+appointment.getCoachEmail()+">")
                .setBodyHtml("<p style=\"font-size:48px;color:#17C671;text-align:center;\">&#x23F3;</p><h1 style=\"text-align:center;\">Appointment Reminder</h1> <h3>Details:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"+
                        appointment.toHTMLString() +
                        "</div>" +
                        "<br/>" +
                        "<a style=\"background-color:#0067F4; color: white; border-radius: 4px; padding: 10px; margin:0 auto; display:block; width: 90%; text-align: center; font-size: 18px; text-decoration:none; \" href=\"" +
                        "https://www.google.com/calendar/render?action=TEMPLATE" +
                        "&text=" + settings.getCenterName().replaceAll(" ", "+") + "+Appointment+With+" + appointment.getStudentName().replaceAll(" ", "+")+
                        "&dates=" +appointment.getStartDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+"/"+appointment.getEndDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+
                        "&details=" + appointment.toString().replace("\n", "%0A").replace(" ","+") +
                        "&location=" + settings.getCenterName().replaceAll(" ","+") +
                        "&sf=true" +
                        "&output=xml\"" +
                        "target=\"_blank\" rel=\"nofollow\">Add to my calendar</a> <br/>");
        mailerClient.send(email);
        /* Student Email */
        email = new Email()
                .setSubject("Appointment Reminder for " + DateFormat.getDateTimeInstance().format(appointment.getStartDate()) + " at the " + settings.getCenterName() )
                .setFrom("Project Burgundy <"+ Application.getConfig().getString("play.mailer.user") +">")
                .addTo(appointment.getStudentName() + "<"+appointment.getStudentEmail()+">")
                .setBodyHtml("<p style=\"font-size:48px;color:#17C671;text-align:center;\">&#x23F3;</p><h1 style=\"text-align:center;\">Appointment Reminder</h1> <h3>Details:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"+
                        appointment.toHTMLString() +
                        "</div>" +
                        "<br/>" +
                        "<a style=\"background-color:#0067F4; color: white; border-radius: 4px; padding: 10px; margin:0 auto; display:block; width: 90%; text-align: center; font-size: 18px; text-decoration:none; \" href=\"" +
                        "https://www.google.com/calendar/render?action=TEMPLATE" +
                        "&text=" + settings.getCenterName().replaceAll(" ", "+") + "+Appointment+With+" + appointment.getCoachName().replaceAll(" ", "+")+
                        "&dates=" +appointment.getStartDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+"/"+appointment.getEndDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+
                        "&details=" + appointment.toString().replace("\n", "%0A").replace(" ","+") +
                        "&location=" + settings.getCenterName().replaceAll(" ","+") +
                        "&sf=true" +
                        "&output=xml\"" +
                        "target=\"_blank\" rel=\"nofollow\">Add to my calendar</a> <br/>");
        mailerClient.send(email);
    }

    public static void sendAppointmentConfirmation(AppointmentsModel appointment) {
        SettingsModel settings = SettingsController.getSettings();
        /* Coach Email */
        Email email = new Email()
                .setSubject("New Appointment With " + appointment.getStudentName() + " on " + DateFormat.getDateTimeInstance().format(appointment.getStartDate()) + " at the " + settings.getCenterName() )
                .setFrom("Project Burgundy <"+ Application.getConfig().getString("play.mailer.user") +">")
                .addTo(appointment.getCoachName() + "<"+appointment.getCoachEmail()+">")
                .setBodyHtml("<p style=\"font-size:48px;color:#17C671;text-align:center;\">&#x2705;</p><h1 style=\"text-align:center;\">You have a new Appointment!</h1> <h3>Details:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"+
                        appointment.toHTMLString() +
                        "</div>" +
                        "<br/>" +
                        "<a style=\"background-color:#0067F4; color: white; border-radius: 4px; padding: 10px; margin:0 auto; display:block; width: 90%; text-align: center; font-size: 18px; text-decoration:none; \" href=\"" +
                        "https://www.google.com/calendar/render?action=TEMPLATE" +
                        "&text=" + settings.getCenterName().replaceAll(" ", "+") + "+Appointment+With+" + appointment.getStudentName().replaceAll(" ", "+")+
                        "&dates=" +appointment.getStartDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+"/"+appointment.getEndDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+
                        "&details=" + appointment.toString().replace("\n", "%0A").replace(" ","+") +
                        "&location=" + settings.getCenterName().replaceAll(" ","+") +
                        "&sf=true" +
                        "&output=xml\"" +
                        "target=\"_blank\" rel=\"nofollow\">Add to my calendar</a> <br/>");
        mailerClient.send(email);
        /* Student Email */
        email = new Email()
                .setSubject("New Appointment With " + appointment.getCoachName() + " on " + DateFormat.getDateTimeInstance().format(appointment.getStartDate()) + " at the " + settings.getCenterName())
                .setFrom("Project Burgundy <"+ Application.getConfig().getString("play.mailer.user") + ">")
                .addTo(appointment.getStudentName() + "<"+appointment.getStudentEmail()+">")
                .setBodyHtml("<p style=\"font-size:48px;color:#17C671;text-align:center;\">&#x2705;</p><h1 style=\"text-align:center;\">You have a new Appointment!</h1> <h3>Details:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"+
                        appointment.toHTMLString() +
                        "</div>" +
                        "<br/>" +
                        "<a style=\"background-color:#0067F4; color: white; border-radius: 4px; padding: 10px; margin:0 auto; display:block; width: 90%; text-align: center; font-size: 18px; text-decoration:none; \" href=\"" +
                        "https://www.google.com/calendar/render?action=TEMPLATE" +
                        "&text=" + settings.getCenterName().replaceAll(" ", "+") + "+Appointment+With+" + appointment.getCoachName().replaceAll(" ", "+")+
                        "&dates=" +appointment.getStartDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+"/"+appointment.getEndDate().toInstant().toString().replaceAll("-","").replaceAll(":","")+
                        "&details=" + appointment.toString().replace("\n", "%0A").replace(" ","+") +
                        "&location=" + settings.getCenterName().replaceAll(" ","+") +
                        "&sf=true" +
                        "&output=xml\"" +
                        "target=\"_blank\" rel=\"nofollow\">Add to my calendar</a> <br/>");
        mailerClient.send(email);
    }

    public static void sendAppointmentCancellation(AppointmentsModel appointment, String cancellationNotes) {
        SettingsModel settings = SettingsController.getSettings();
        /* Coach Email */
        Email email = new Email()
                .setSubject("Appointment With " + appointment.getStudentName() + " on " + DateFormat.getDateTimeInstance().format(appointment.getStartDate()) + " at the " + settings.getCenterName() +" CANCELLED")
                .setFrom("Project Burgundy <"+ Application.getConfig().getString("play.mailer.user") +">")
                .addTo(appointment.getCoachName() + "<"+appointment.getCoachEmail()+">")
                .setBodyHtml("<p style=\"font-size:48px;color:#C4183C;text-align:center;\">&#10060;</p><h1 style=\"text-align:center;\">Your Appointment Has Been Cancelled</h1> <h3>Details:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"+
                        appointment.toHTMLString() +
                        "</div>" +
                        "<h3>Reason for cancellation:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"+
                        cancellationNotes +
                        "</div>" +
                        "<br/>" +
                        "<a style=\"background-color:#0067F4; color: white; border-radius: 4px; padding: 10px; margin:0 auto; display:block; width: 90%; text-align: center; font-size: 18px; text-decoration:none; \" " +
                        "href=\"http://" + Application.getConfig().getString("siteUrl") + "\">Schedule a new appointment</a> <br/>");
        mailerClient.send(email);
        /* Student Email */
        email = new Email()
                .setSubject("Appointment With " + appointment.getCoachName()+ " on " + DateFormat.getDateTimeInstance().format(appointment.getStartDate()) + " at the " + settings.getCenterName() + " CANCELLED")
                .setFrom("Project Burgundy <"+ Application.getConfig().getString("play.mailer.user") +">")
                .addTo(appointment.getStudentName() + "<"+appointment.getStudentEmail()+">")
                .setBodyHtml("<p style=\"font-size:48px;color:#C4183C;text-align:center;\">&#10060;</p><h1 style=\"text-align:center;\">Your Appointment Has Been Cancelled</h1> <h3>Details:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"+
                        appointment.toHTMLString() +
                        "</div>" +
                        "<h3>Reason for cancellation:</h3>" +
                        "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">"+
                        cancellationNotes +
                        "</div>" +
                        "<br/>" +
                        "<a style=\"background-color:#0067F4; color: white; border-radius: 4px; padding: 10px; margin:0 auto; display:block; width: 90%; text-align: center; font-size: 18px; text-decoration:none; \" " +
                        "href=\"http://" + Application.getConfig().getString("siteUrl")  +"\">Schedule a new appointment</a> <br/>");
        mailerClient.send(email);
    }
}