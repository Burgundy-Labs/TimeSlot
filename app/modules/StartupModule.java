package modules;

import ApplicationComponents.*;
import com.google.inject.AbstractModule;
import controllers.Databases.FirestoreDB;

public class StartupModule extends AbstractModule {

    protected void configure() {
     /* Start required handlers and services */
     bind(ErrorHandler.class).asEagerSingleton();
     bind(CompressorFilter.class).asEagerSingleton();
     bind(FirestoreDB.class).asEagerSingleton();
     bind(Application.class).asEagerSingleton();
     bind(MailerService.class).asEagerSingleton();
     bind(EmailScheduler.class).asEagerSingleton();
    }
}
