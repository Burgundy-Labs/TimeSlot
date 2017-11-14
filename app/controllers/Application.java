package controllers;

import controllers.ApplicationComponents.MenuLinks;

import java.util.Arrays;
import java.util.List;

public class Application {
    public static List<MenuLinks> menuLinks = Arrays.asList(
            new MenuLinks("/Account", "Account", "account_circle", "Manage your account settings and preferences."),
            new MenuLinks("/Dashboard", "Dashboard", "dashboard", "Something about the dashboard."),
            new MenuLinks("/Appointments", "Appointments", "schedule", "View / manage your upcoming and past appointments."),
            new MenuLinks("/Coaches", "Coaches", "recent_actors", "View all coaches working at the center."),
            new MenuLinks("/Center", "Center", "school", "View all coaches' appointments at the center."),
            new MenuLinks("/Settings","Site Settings", "settings","Manage settings related to the center")
    );

}



