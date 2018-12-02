package application_components;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import databases.GroupsDB;
import models.GroupsModel;
import play.Environment;

import java.util.*;

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
        this.RunStartupCode();
    }

    /// Test any start-up code here.
    private void RunStartupCode() {
//        System.out.println("Running startup code.");
//        List<String> coaches = new ArrayList<>();
//        coaches.add("cnPylOXnLpSANT2v943wbnfXIsU2");
//
//        List<String> students = new ArrayList<>();
//        students.add("QDig3Ue5lgdnTNpQMa3fj9ArBCF2");
//
//        List<String> request = new ArrayList<>();
//
//        List<Integer> recur = new ArrayList<>();
//        recur.add(4);
//
//        Date startDate = new GregorianCalendar(2018, Calendar.NOVEMBER, 25, 0, 0).getTime();
//        Date endDate = new GregorianCalendar(2018, Calendar.DECEMBER, 25, 0, 0).getTime();
//        Date startTime = new GregorianCalendar(0, 0, 0, 11, 20).getTime();
//        Date endTime = new GregorianCalendar(0, 0, 0, 12, 20).getTime();
//        GroupsModel group = new GroupsModel(coaches, students, request, null, recur,
//                "this is a group", 10, startDate, endDate, startTime, endTime, "group", "rekhi",
//                true);
//        GroupsDB db = new GroupsDB();
//        db.addOrUpdate(group);
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



