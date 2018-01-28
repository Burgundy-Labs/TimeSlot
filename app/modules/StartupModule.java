package modules;

import controllers.ApplicationComponents.*;
import com.google.inject.AbstractModule;
import controllers.Application;
import controllers.Databases.FirestoreDB;

public class StartupModule extends AbstractModule {

    protected void configure() {
     /* Start the database service */
     bind(FirestoreDB.class).asEagerSingleton();
     bind(Application.class).asEagerSingleton();
     bind(MailerService.class).asEagerSingleton();
     bind(EmailScheduler.class).asEagerSingleton();
     /* Define default roles for project */
     new Roles("Coach");
     new Roles("Admin");
     new Roles("Student");
    }
}
