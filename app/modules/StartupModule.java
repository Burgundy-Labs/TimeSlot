package modules;

import com.google.cloud.firestore.Firestore;
import com.google.inject.AbstractModule;
import controllers.Application;
import controllers.ApplicationComponents.Roles;
import controllers.Databases.FirestoreDB;
import play.Environment;
import play.api.Play;

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
