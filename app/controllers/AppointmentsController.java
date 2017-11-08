package controllers;

import models.AppointmentsModel;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

public class AppointmentsController extends Controller {

    public Result index() {
        return ok(views.html.appointments.render());
    }

    public List<AppointmentsModel> getAppointments(String userId){
        List<AppointmentsModel> appointments = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            AppointmentsModel appointment = new AppointmentsModel();
            appointment.setStudentName("John Doe " + i);
            appointment.setAppointmentNotes("Notes for appointment # " + i);
            appointment.setStartDate(new Date());
            appointment.setEndDate(new Date());
            appointments.add(appointment);
        }
        return appointments;
    }

}
