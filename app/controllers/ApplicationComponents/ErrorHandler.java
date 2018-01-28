package controllers.ApplicationComponents;

import com.typesafe.config.Config;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {
    @Inject
    public ErrorHandler(Config config, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        switch (statusCode) {
            case 404:
                return CompletableFuture.completedFuture(Results.notFound(views.html.error_pages.notfound.render()));
            case 401:
                return CompletableFuture.completedFuture(Results.unauthorized(views.html.error_pages.unauthorized.render()));
            case 403:
                return CompletableFuture.completedFuture(Results.forbidden(views.html.error_pages.unauthorized.render()));
            default:
                break;
        }
        return super.onClientError(request, statusCode, message);
    }
    protected CompletionStage<Result> onForbidden(RequestHeader request, String message) {
        return CompletableFuture.completedFuture(
                Results.forbidden("You're not allowed to access this resource.")
        );
    }
    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        return CompletableFuture.completedFuture(
                Results.internalServerError(views.html.error_pages.servererror.render())
        );
    }
}