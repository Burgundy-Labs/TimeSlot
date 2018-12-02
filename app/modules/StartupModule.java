package modules;

import application_components.*;
import application_components.mailing.EmailScheduler;
import application_components.mailing.MailerService;
import com.google.inject.AbstractModule;
import databases.FirestoreHandler;

public class StartupModule extends AbstractModule {
    protected void configure() {
     /* Start required handlers and services */
     bind(ErrorHandler.class).asEagerSingleton();
     bind(CompressorFilter.class).asEagerSingleton();
     bind(FirestoreHandler.class).asEagerSingleton();
     bind(MailerService.class).asEagerSingleton();
     bind(EmailScheduler.class).asEagerSingleton();
     bind(Application.class).asEagerSingleton(); // Keep last for RunStartupCode to have access to all DI modules
    }
}
