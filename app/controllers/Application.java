package controllers;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import controllers.ApplicationComponents.MenuLinks;
import play.Environment;

import java.util.Arrays;
import java.util.List;

public class Application {
    private static Config config;
    private static Environment environment;

    @Inject
    public Application(Config config, Environment environment) {
        Application.config = config;
        Application.environment = environment;
    }

    public static Environment getEnvironment(){
        return environment;
    }

    public static Config getConfig(){
        return config;
    }

    public static List<MenuLinks> menuLinks = Arrays.asList(
            new MenuLinks("/Account", "Account", "account_circle", "Manage your account settings and preferences.", true, true),
            new MenuLinks("/Dashboard", "Dashboard", "dashboard", "Something about the dashboard.", false, false),
            new MenuLinks("/Appointments", "Appointments", "schedule", "View / manage your upcoming and past appointments.", false, false),
            new MenuLinks("/Coaches", "Coaches", "recent_actors", "View all users working at the center.",false, false),
            new MenuLinks("/Center", "Center", "school", "View all users' appointments at the center.",true,false ),
            new MenuLinks("/Settings","Site Settings", "settings","Manage settings related to the center",true,false ) ,
            new MenuLinks("/Reports", "Reports", "assessment", "View reports and statistics about the center", true, false)
    );
}



