package application_components.mailing;

import application_components.mailing.templates.html.general;
import models.AppointmentsModel;
import models.GroupsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* Allows the creation of email HTML based on templates and
* email models. Builder methods compose email models with relevant
* content from parameters.
*/
public class EmailBuilder implements IconInterface{

    public String appointmentReminder(AppointmentsModel appointment) {
        /* Define basic email model properties */
        EmailModel model = new EmailModel();
        model.setIcon(IconInterface.ALARM_CLOCK);
        model.setTitle("Appointment Reminder");

        /* Compose the sections of an email (section title, content) */
        HashMap<String,String> sections = new HashMap<>();
        sections.put("Appointment Details", appointment.toHTMLString());
        model.setEmailBody(new EmailModel.EmailBody(sections));

        /* Add call-to-action buttons at bottom of email */
        List<EmailModel.EmailButton> emailButtons = new ArrayList<>();
        emailButtons.add(new EmailModel.EmailButton("Add to Calendar", ""));
        model.setEmailButtons(emailButtons);

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
