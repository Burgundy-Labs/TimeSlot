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
        /* Define default roles for project */
        new Roles("Coach");
        new Roles("Admin");
        new Roles("Student");
    }
}
