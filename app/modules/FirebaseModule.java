package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import controllers.Firebase;

@Singleton
public class FirebaseModule extends AbstractModule {

    @Inject
    protected void configure() {
        Firebase.initialize();
    }
}
