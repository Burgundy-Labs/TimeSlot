package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationComponents.AppointmentTypes;
import controllers.Databases.AppointmentsDB;
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
        for (int i = 0; i < 100; i++) {
            AppointmentsModel appointment = new AppointmentsModel();
            appointment.setStudentId("John Doe " + i);
            appointment.setAppointmentNotes("Notes for appointment # " + i);
            appointment.setStartDate(new Date().toString());
            appointment.setEndDate(new Date().toString());
            appointments.add(appointment);
        }
        return appointments;
    }

    public Result makeAppointment() {
        return ok(views.html.makeAppointment.render());
    }

    public Result createAppointment() {
        /* Get user object from request */
        JsonNode json = request().body().asJson();
        /* Get user from json request */
        AppointmentsModel appointment = new AppointmentsModel();
        appointment.setAppointmentId(json.findPath("appointmentId").textValue());
        appointment.setCoachId(json.findPath("coachId").textValue());
        appointment.setStudentId(json.findPath("studentId").textValue());
        appointment.setAppointmentType(json.findPath("appointmentType").textValue());
        appointment.setStartDate(json.findPath("startDate").textValue());
        appointment.setEndDate(json.findPath("endDate").textValue());
        appointment.setAppointmentNotes(json.findPath("appointmentNotes").textValue());
        appointment.setPresent(Boolean.getBoolean(json.findPath("present").textValue()));
        /* Check if user is in DB */
        AppointmentsDB.addAppointment(appointment);
        return ok();
    }

    public Result cancelAppointment(){
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        AppointmentsDB.removeAppointment(appointmentId);
        return ok();
    }

    public Result updateCoachNotes(){
        JsonNode json = request().body().asJson();
        String appointmentId = json.findPath("appointmentId").textValue();
        String coachNotes = json.findPath("coachNotes").textValue();
        AppointmentsModel appointment = AppointmentsDB.getAppointment(appointmentId);
        appointment.setCoachNotes(coachNotes);
        AppointmentsDB.addAppointment(appointment);
        return ok();
    }
}
