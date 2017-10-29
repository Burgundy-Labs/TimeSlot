package controllers;

import controllers.ApplicationComponents.MenuLink;
import java.util.Arrays;
import java.util.List;

import static play.mvc.Controller.session;

public class Application {
    public static List<MenuLink> menuLinks = Arrays.asList(
            new MenuLink("/Account", "Account", "account_circle", "Manage your account settings and preferences."),
            new MenuLink("/Dashboard", "Dashboard", "dashboard", "Something about the dashboard."),
            new MenuLink("/Appointments", "Appointments", "schedule", "View / manage your upcoming and past appointments."),
            new MenuLink("/Coaches", "Coaches", "recent_actors", "View all coaches working at the center."),
            new MenuLink("/Center", "Center", "school", "View all coaches' appointments at the center."),
            new MenuLink("/Settings","Site Settings", "settings","Manage settings related to the center")
    );
    /* Returns whether or not a signed-in user is an administrator */
    public static boolean isAdmin() {
        /* Get user by userID */
        String userId = session("signedInUser");
        return userId != null && userId.equals("akAvM1RfLMgjezbC6Z9pGCJ7nCr1");
    }
}




