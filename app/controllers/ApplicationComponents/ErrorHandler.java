package controllers.ApplicationComponents;

import com.typesafe.config.Config;
import controllers.Application;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.http.HttpErrorHandler;
import play.mvc.*;
import play.mvc.Http.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler implements HttpErrorHandler {
    @Inject
    public ErrorHandler(Config config, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        switch(statusCode){
            case 404:
                if(Application.getEnvironment().isDev()){
                    return CompletableFuture.completedFuture(
                            Results.notFound(views.html.error_pages.notfound.render())
                    );
                } else {
                    return CompletableFuture.completedFuture(
                            Results.notFound(views.html.error_pages.notfound.render())
                    );
                }
            case 500:
                break;
            default:
                break;
        }
        return super.onClientError(request, statusCode, message);
    }

    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        return CompletableFuture.completedFuture(
                Results.internalServerError("TEST " + exception.getMessage())
        );
    }
}