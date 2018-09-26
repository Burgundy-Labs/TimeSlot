package controllers;

import application_components.mailing.MailerService;
import databases.AppointmentsDB;
import databases.SettingsDB;
import databases.UserDB;
import models.ServiceModel;
import models.SettingsModel;
import models.UsersModel;
import play.mvc.Controller;

import java.util.List;

public class BaseController extends Controller {
    /**
     * Global instances of DB's & Services usable by all controllers
     */
    AppointmentsDB appointmentsDB = new AppointmentsDB();
    SettingsDB settingsDB = new SettingsDB();
    UserDB userDB = new UserDB();

    /* Roles used throughout TimeSlot */
    String[] roles = { "Student" , "Coach", "Admin" };

    public UsersModel getCurrentUser() {
        String s = session("currentUser");
        if (s == null || s.isEmpty()) {
            UsersModel u = new UsersModel();
            u.setRole("Student");
            return u;
        }
        return new UserDB().get(s).orElseThrow(NullPointerException::new);
    }

    /**
     * Returns the current SettingsModel for the center via settingsDB
     * @return the SettingsModel for this learning center
     */
    public SettingsModel getSettings() {
        return settingsDB.get(null).orElseThrow(NullPointerException::new);
    }

    /**
     * Returns a list of Services being used in the Database
     * @return a list of ServiceModel's from the settingsDB
     */
    public List<ServiceModel> getServices() {
        return settingsDB.getServices();
    }


    /**
     * Attaches a message to the flash session which is then parsed & displayed
     * on all pages via main.scala.html & the _addMessage.scala.html partial.
     * @param messageType The type of message to be displayed as, ie. ERROR, SUCCESS
     * @param message The text the alert should display
     */
    void addMessage(MessageType messageType, String message) {
        flash().put(messageType.toString(), message);
    }

    public enum MessageType {
        ERROR,
        INFO,
        SUCCESS,
        WARNING
    }
}
