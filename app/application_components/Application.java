package application_components;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import play.Environment;

import java.util.Arrays;
import java.util.List;

/* TODO look into better use of DI to remove static methods */
public class Application {
    private static Config config;
    private static Environment environment;

    public static List<MenuLinks> menuLinks = Arrays.asList(
            new MenuLinks("/Account", "Account", "user", "Manage your account settings and preferences.", false, false),
            new MenuLinks("/Dashboard", "Dashboard", "shapes", "Something about the dashboard.", false, false),
            new MenuLinks("/Appointments", "Appointments", "user-clock", "View / manage your upcoming and past appointments.", false, false),
            new MenuLinks("/Users", "Users", "chalkboard-teacher", "View all users at the center.",true, true),
            new MenuLinks("/Center", "Center", "book-reader", "View all users' appointments at the center.",true,false ),
            new MenuLinks("/Settings","Site Settings", "cog","Manage settings related to the center",true,false ) ,
            new MenuLinks("/Reports", "Reports", "chart-pie", "View reports and statistics about the center", true, false)
    );

    @Inject
    public Application(Config config, Environment environment) {
        Application.config = config;
        Application.environment = environment;
    }

    public Application() {

    }

    public static Environment getEnvironment(){
        return environment;
    }

    public static Config getConfig(){
        return config;
    }

}



