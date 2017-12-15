package controllers;

import controllers.ApplicationComponents.MenuLinks;

import java.util.Arrays;
import java.util.List;

public class Application {
    public static List<MenuLinks> menuLinks = Arrays.asList(
            new MenuLinks("/Account", "Account", "account_circle", "Manage your account settings and preferences.", false),
            new MenuLinks("/Dashboard", "Dashboard", "dashboard", "Something about the dashboard.", false),
            new MenuLinks("/Appointments", "Appointments", "schedule", "View / manage your upcoming and past appointments.", false),
            new MenuLinks("/Coaches", "Coaches", "recent_actors", "View all coaches working at the center.",false),
            new MenuLinks("/Center", "Center", "school", "View all coaches' appointments at the center.",false),
            new MenuLinks("/Settings","Site Settings", "settings","Manage settings related to the center",true),
            new MenuLinks("/Reports", "Reports", "assessment", "View reports and statistics about the center", true)
    );

}



