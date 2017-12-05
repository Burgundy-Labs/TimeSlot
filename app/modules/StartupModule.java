package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import controllers.ApplicationComponents.AppointmentTypes;
import controllers.ApplicationComponents.MenuLinks;
import controllers.ApplicationComponents.Roles;
import controllers.ApplicationComponents.Services;
import controllers.Databases.FirestoreDB;
import controllers.MailerService;

@Singleton
public class StartupModule extends AbstractModule {

    @Inject
    protected void configure() {
        /* Start the database service */
        FirestoreDB.initialize();
        /* Define menu links */
        new MenuLinks("/Account", "Account", "account_circle", "Manage your account settings and preferences.");
        new MenuLinks("/Dashboard", "Dashboard", "dashboard", "Something about the dashboard.");
        new MenuLinks("/Appointments", "Appointments", "schedule", "View / manage your upcoming and past appointments.");
        new MenuLinks("/Coaches", "Coaches", "recent_actors", "View all coaches working at the center.");
        new MenuLinks("/Center", "Center", "school", "View all coaches' appointments at the center.");
        new MenuLinks("/Settings","Site Settings", "settings","Manage settings related to the center");

        /* Define default roles for project */
        new Roles("Coach");
        new Roles("Admin");
        new Roles("Student");

        /* Define default appointment types */
        new AppointmentTypes("One-Time", "One-on-one with a coach in the center.");
        new AppointmentTypes("Weekly" ,"A recurring weekly regular appointment.");
        new AppointmentTypes("Online", "Submit a paper for a coach to read and receive comments back.");

        /* Define default services */
        new Services("Writing");
        new Services("Speaking");
        new Services("Studying");

    }
}
