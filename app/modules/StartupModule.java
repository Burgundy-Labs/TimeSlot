package modules;

import com.google.cloud.firestore.Firestore;
import com.google.inject.AbstractModule;
import controllers.ApplicationComponents.Roles;
import controllers.Databases.FirestoreDB;

public class StartupModule extends AbstractModule {

    protected void configure() {
     /* Start the database service */
        bind(Firestore.class).toProvider(FirestoreDB.class).asEagerSingleton();
        requestStaticInjection(FirestoreDB.class);
        /* Define default roles for project */
        new Roles("Coach");
        new Roles("Admin");
        new Roles("Student");
    }
}
