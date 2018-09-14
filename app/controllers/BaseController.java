package controllers;

import application_components.mailing.MailerService;
import databases.AppointmentsDB;
import databases.SettingsDB;
import databases.UserDB;
import play.mvc.Controller;

public class BaseController extends Controller {

    /**
     * Global instances of DB's & Services usable by all controllers
     */
    AppointmentsDB appointmentsDB = new AppointmentsDB();
    SettingsDB settingsDB = new SettingsDB();
    UserDB userDB = new UserDB();
    MailerService mailerService = new MailerService();
    UserController userController = new UserController();

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
