package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import controllers.FirestoreDB;

@Singleton
public class FirestoreModule extends AbstractModule {

    @Inject
    protected void configure() {
        FirestoreDB.initialize();
    }
}
