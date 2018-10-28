package application_components.mailing;

import application_components.mailing.templates.html.general;
import models.AppointmentsModel;
import models.SettingsModel;
import application_components.Application;
import models.GroupsModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* Allows the creation of email HTML based on templates and
* email models. Builder methods compose email models with relevant
* content from parameters.
*/
public class EmailBuilder implements IconInterface{

    public String appointmentReminder(AppointmentsModel appointment, SettingsModel settings, String type) {
        /* Define basic email model properties */
        //TODO
        EmailModel model = new EmailModel();
        model.setIcon(IconInterface.ALARM_CLOCK);
        model.setTitle("Appointment Reminder");

        /* Compose the sections of an email (section title, content) */
        HashMap<String,String> sections = new HashMap<>();
        sections.put("Appointment Details", appointment.toHTMLString());
        model.setEmailBody(new EmailModel.EmailBody(sections));

        /* Add call-to-action buttons at bottom of email */
        List<EmailModel.EmailButton> emailButtons = new ArrayList<>();
        if("Coach".equals(type)){
            emailButtons.add(new EmailModel.EmailButton("Add to Calendar", "https://www.google.com/calendar/render?action=TEMPLATE" +
                    "&text=" + settings.getCenterName().replaceAll(" ", "+") + "+Appointment+With+" + appointment.getCoachName().replaceAll(" ", "+") +
                    "&dates=" + appointment.getStartDate().toInstant().toString().replaceAll("-", "").replaceAll(":", "") +
                    "/" + appointment.getEndDate().toInstant().toString().replaceAll("-", "").replaceAll(":", "") +
                    "&details=" + appointment.toString().replace("\n", "%0A").replace(" ", "+") +
                    "&location=" + settings.getCenterName().replaceAll(" ", "+") +
                    "&sf=true" +
                    "&output=xml\""));
        } else if("Student".equals(type)) {
            emailButtons.add(new EmailModel.EmailButton("Add to Calendar", "https://www.google.com/calendar/render?action=TEMPLATE" +
                    "&text=" + settings.getCenterName().replaceAll(" ", "+") + "+Appointment+With+" + appointment.getStudentName().replaceAll(" ", "+") +
                    "&dates=" + appointment.getStartDate().toInstant().toString().replaceAll("-", "").replaceAll(":", "") +
                    "/" + appointment.getEndDate().toInstant().toString().replaceAll("-", "").replaceAll(":", "") +
                    "&details=" + appointment.toString().replace("\n", "%0A").replace(" ", "+") +
                    "&location=" + settings.getCenterName().replaceAll(" ", "+") +
                    "&sf=true" +
                    "&output=xml\""));
        }

        model.setEmailButtons(emailButtons);
        model.setEmailFooter(new EmailModel.EmailFooter(""));
        /* Generates HTML from template with passed model */
        return general.render(model).toString();
    }

    public String appointmentConfirmation(AppointmentsModel appointment, SettingsModel settings, String type) {
        /* Define basic email model properties */
        EmailModel model = new EmailModel();
        model.setIcon(IconInterface.ALARM_CLOCK);
        model.setTitle("Appointment Confirmation");

        /* Compose the sections of an email (section title, content) */
        HashMap<String,String> sections = new HashMap<>();
        sections.put("Appointment Details", appointment.toHTMLString());
        model.setEmailBody(new EmailModel.EmailBody(sections));

        /* Add call-to-action buttons at bottom of email */
        List<EmailModel.EmailButton> emailButtons = new ArrayList<>();
        if("Coach".equals(type)) {
            emailButtons.add(new EmailModel.EmailButton("Add to Calendar", "https://www.google.com/calendar/render?action=TEMPLATE" +
                    "&text=" + settings.getCenterName().replaceAll(" ", "+") + "+Appointment+With+" + appointment.getStudentName().replaceAll(" ", "+") +
                    "&dates=" + appointment.getStartDate().toInstant().toString().replaceAll("-", "").replaceAll(":", "") + "/" + appointment.getEndDate().toInstant().toString().replaceAll("-", "").replaceAll(":", "") +
                    "&details=" + appointment.toString().replace("\n", "%0A").replace(" ", "+") +
                    "&location=" + settings.getCenterName().replaceAll(" ", "+") +
                    "&sf=true" +
                    "&output=xml\""));
        } else if("Student".equals(type)){
            emailButtons.add(new EmailModel.EmailButton("Add to Calendar", "https://www.google.com/calendar/render?action=TEMPLATE" +
                    "&text=" + settings.getCenterName().replaceAll(" ", "+") + "+Appointment+With+" + appointment.getCoachName().replaceAll(" ", "+") +
                    "&dates=" + appointment.getStartDate().toInstant().toString().replaceAll("-", "").replaceAll(":", "") + "/" + appointment.getEndDate().toInstant().toString().replaceAll("-", "").replaceAll(":", "") +
                    "&details=" + appointment.toString().replace("\n", "%0A").replace(" ", "+") +
                    "&location=" + settings.getCenterName().replaceAll(" ", "+") +
                    "&sf=true" +
                    "&output=xml\""));
        }
        model.setEmailButtons(emailButtons);
        model.setEmailFooter(new EmailModel.EmailFooter(""));

        /* Generates HTML from template with passed model */
        return general.render(model).toString();
    }

    public String appointmentCancellation(AppointmentsModel appointment, String cancellationNotes) {
        /* Define basic email model properties */
        EmailModel model = new EmailModel();
        model.setIcon(IconInterface.ALARM_CLOCK);
        model.setTitle("Appointment Cancellation");

        /* Compose the sections of an email (section title, content) */
        HashMap<String,String> sections = new HashMap<>();
        sections.put("Appointment Details", appointment.toHTMLString());
        sections.put("Reason for cancellation", "<div style=\"background-color:#EFF0F1; border-radius:4px; padding: 10px;\">" +
                cancellationNotes +
                "</div>");
        model.setEmailBody(new EmailModel.EmailBody(sections));

        /* Add call-to-action buttons at bottom of email */
        List<EmailModel.EmailButton> emailButtons = new ArrayList<>();
        emailButtons.add(new EmailModel.EmailButton("Schedule a new appointment", "http://" + Application.getConfig().getString("siteUrl")));
        model.setEmailButtons(emailButtons);
        model.setEmailFooter(new EmailModel.EmailFooter(""));

        /* Generates HTML from template with passed model */
        return general.render(model).toString();
    }

    public String groupAppointmentEmail(GroupsModel appointment){
        /* Define basic email model properties */
        EmailModel model = new EmailModel();
        model.setIcon(IconInterface.ALARM_CLOCK);
        model.setTitle("Appointment Reminder");

        /* Compose the sections of an email (section title, content) */
        HashMap<String,String> sections = new HashMap<>();
        sections.put("Appointment Details", "");
        model.setEmailBody(new EmailModel.EmailBody(sections));

        /* Add call-to-action buttons at bottom of email */
        List<EmailModel.EmailButton> emailButtons = new ArrayList<>();
        emailButtons.add(new EmailModel.EmailButton("Add to Calendar", ""));
        model.setEmailButtons(emailButtons);

        /* Generates HTML from template with passed model */
        return general.render(model).toString();
    }
}
