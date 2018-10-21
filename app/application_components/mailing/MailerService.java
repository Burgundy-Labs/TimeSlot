package application_components.mailing;

import application_components.Application;
import com.google.inject.Inject;
import controllers.SettingsController;
import models.AppointmentsModel;
import models.GroupsModel;
import models.SettingsModel;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MailerService {
    private SettingsController settingsController = new SettingsController();
    private static MailerClient mailerClient;

    @Inject
    public MailerService(MailerClient mailerClient) {
        MailerService.mailerClient = mailerClient;
    }

    public MailerService() { }

    public void sendEmail(String subject, String fromName, String fromEmail, String toName, String toEmail, String bodyHtml) {
        Email email = new Email()
                .setSubject(subject)
                .setFrom(fromName + "<" + fromEmail + ">")
                .addTo(toName + "<" + toEmail + ">")
                .setBodyHtml(bodyHtml);
        mailerClient.send(email);
    }


    public void sendAppointmentReminder(HashMap<String, ArrayList<AppointmentsModel>> appointments, String type) {
        if (appointments.size() == 0) {
            return;
        }
        SettingsModel settings = settingsController.getSettings();
        /* Coach Email */
        for (String l : appointments.keySet()) {
            StringBuilder emailBody = new StringBuilder();
            for (AppointmentsModel a : appointments.get(l)) {
                emailBody.append(new EmailBuilder().appointmentReminder(a, settings, type));
            }
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("EEEE MMMMM dd");
            String dateString = format.format(date);
            Email email = new Email()
                    .setSubject("Appointment Reminder: " + dateString + " at the " + settings.getCenterName())
                    .setFrom("TimeSlot <" + Application.getConfig().getString("play.mailer.user") + ">")
                    .setBodyHtml(emailBody.toString());
            if ("Student".equals(type)) {
                email.addTo(appointments.get(l).get(0).getStudentName() + "<" + appointments.get(l).get(0).getStudentEmail() + ">");

            } else if ("Coach".equals(type)) {
                email.addTo(appointments.get(l).get(0).getCoachName() + "<" + appointments.get(l).get(0).getCoachEmail() + ">");
            }
            mailerClient.send(email);
        }
    }

    public void sendAppointmentConfirmation(AppointmentsModel appointment) {
        SettingsModel settings = settingsController.getSettings();
        /* Coach Email */
        Email email = new Email()
                .setSubject("New Appointment With " + appointment.getStudentName() + " on " + DateFormat.getDateTimeInstance().format(appointment.getStartDate()) + " at the " + settings.getCenterName())
                .setFrom("TimeSlot <" + Application.getConfig().getString("play.mailer.user") + ">")
                .addTo(appointment.getCoachName() + "<" + appointment.getCoachEmail() + ">")
                .setBodyHtml(new EmailBuilder().appointmentConfirmation(appointment, settings, "Coach"));
        mailerClient.send(email);
        /* Student Email */
        email = new Email()
                .setSubject("New Appointment With " + appointment.getCoachName() + " on " + DateFormat.getDateTimeInstance().format(appointment.getStartDate()) + " at the " + settings.getCenterName())
                .setFrom("TimeSlot <" + Application.getConfig().getString("play.mailer.user") + ">")
                .addTo(appointment.getStudentName() + "<" + appointment.getStudentEmail() + ">")
                .setBodyHtml(new EmailBuilder().appointmentConfirmation(appointment, settings, "Student"));
        mailerClient.send(email);
    }

    public void sendAppointmentCancellation(AppointmentsModel appointment, String cancellationNotes) {
        SettingsModel settings = settingsController.getSettings();
        /* Coach Email */
        Email email = new Email()
                .setSubject("Appointment With " + appointment.getStudentName() + " on " + DateFormat.getDateTimeInstance().format(appointment.getStartDate()) + " at the " + settings.getCenterName() + " CANCELLED")
                .setFrom("TimeSlot <" + Application.getConfig().getString("play.mailer.user") + ">")
                .addTo(appointment.getCoachName() + "<" + appointment.getCoachEmail() + ">")
                .setBodyHtml(new EmailBuilder().appointmentCancellation(appointment, cancellationNotes));
        mailerClient.send(email);
        /* Student Email */
        email = new Email()
                .setSubject("Appointment With " + appointment.getCoachName() + " on " + DateFormat.getDateTimeInstance().format(appointment.getStartDate()) + " at the " + settings.getCenterName() + " CANCELLED")
                .setFrom("TimeSlot <" + Application.getConfig().getString("play.mailer.user") + ">")
                .addTo(appointment.getStudentName() + "<" + appointment.getStudentEmail() + ">")
                .setBodyHtml(new EmailBuilder().appointmentCancellation(appointment, cancellationNotes));
        mailerClient.send(email);
    }

    public void sendGroupAppointmentEmail(GroupsModel appointment){
        SettingsModel settings = settingsController.getSettings();
        Email email = new Email().setSubject("").setFrom("").addTo("").setBodyHtml("");
        mailerClient.send(email);
    }
}