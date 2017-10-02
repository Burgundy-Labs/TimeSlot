package controllers;

import models.AppointmentsModel;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.*;

public class AppointmentsController extends Controller {

    public Result index() {
        List<AppointmentsModel> appointments = new ArrayList<>();
        for (int i = 0; i < 10 ; i++) {
            AppointmentsModel appointment = new AppointmentsModel();
            appointment.setStudentName("John Doe " + i);
            appointment.setAppointmentNotes("Notes for appointment # " + i);
            appointments.add(appointment);
        }
        return ok(views.html.appointments.render(appointments));
    }

}
