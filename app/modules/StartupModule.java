package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import controllers.ApplicationComponents.AppointmentType;
import controllers.ApplicationComponents.Role;
import controllers.Databases.FirestoreDB;
import scala.App;

@Singleton
public class StartupModule extends AbstractModule {

    @Inject
    protected void configure() {
        FirestoreDB.initialize();

        /* Define default roles for project */
        new Role("Coach");
        new Role("Admin");
        new Role("Student");

        /* Define default appointment types */
        new AppointmentType("One-Time", "One-on-one with a coach in the center.");
        new AppointmentType("Weekly" ,"A recurring weekly regular appointment.");
        new AppointmentType("Online", "Submit a paper for a coach to read and receive comments back.");
    }
}
