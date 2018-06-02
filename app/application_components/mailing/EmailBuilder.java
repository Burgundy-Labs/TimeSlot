package application_components.mailing;

import application_components.mailing.templates.html.general;
import models.AppointmentsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmailBuilder implements IconInterface{

    public String AppointmentReminder(AppointmentsModel appointment) {
        EmailModel model = new EmailModel();

        model.setIcon(IconInterface.ALARM_CLOCK);
        model.setTitle("Appointment Reminder");

        HashMap<String,String> sections = new HashMap<>();
        sections.put("Appointment Details", appointment.toHTMLString());
        model.setBody(new EmailModel.EmailBody(sections));

        List<EmailModel.EmailButton> emailButtons = new ArrayList<>();
        emailButtons.add(new EmailModel.EmailButton("Add to Calendar", ""));
        model.setEmailButtons(emailButtons);
        return general.render(model).toString();
    }
}
