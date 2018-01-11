package modules;

import com.google.inject.AbstractModule;
import controllers.Application;
import controllers.ApplicationComponents.Roles;
import controllers.Databases.FirestoreDB;

public class StartupModule extends AbstractModule {

    protected void configure() {
     /* Start the database service */
        bind(FirestoreDB.class).asEagerSingleton();
        bind(Application.class).asEagerSingleton();
        /* Define default roles for project */
        new Roles("Coach");
        new Roles("Admin");
        new Roles("Student");
    }
}
