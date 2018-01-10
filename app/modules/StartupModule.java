package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import controllers.ApplicationComponents.Roles;
import controllers.Databases.FirestoreDB;

import java.io.IOException;

@Singleton
public class StartupModule extends AbstractModule {

    @Inject
    protected void configure() {
        /* Start the database service */
        bind(FirestoreDB.class).asEagerSingleton();
        /* Define default roles for project */
        new Roles("Coach");
        new Roles("Admin");
        new Roles("Student");
    }
}
