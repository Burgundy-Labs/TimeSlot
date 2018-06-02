package application_components;

import com.typesafe.config.Config;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.http.HttpErrorHandler;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler implements HttpErrorHandler {
    private HttpExecutionContext httpExecutionContext;

    @Inject
    public ErrorHandler(Config config, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes, HttpExecutionContext ec) {
        super(config, environment, sourceMapper, routes);
        this.httpExecutionContext = ec;
    }

    @Override
    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        switch (statusCode) {
            case 401:
                return CompletableFuture.completedFuture(null).thenApplyAsync(a -> Results.unauthorized(views.html.error_pages.unauthorized.render()), httpExecutionContext.current());
            case 403:
                return CompletableFuture.completedFuture(null).thenApplyAsync(a -> Results.forbidden(views.html.error_pages.unauthorized.render()), httpExecutionContext.current());
            case 404:
                return CompletableFuture.completedFuture(null).thenApplyAsync(a -> Results.notFound(views.html.error_pages.notfound.render()), httpExecutionContext.current());
            default:
                break;
        }
        return super.onClientError(request, statusCode, message);
    }

    @Override
    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        return CompletableFuture.completedFuture(exception).thenApplyAsync(a -> Results.internalServerError(views.html.error_pages.servererror.render(exception))
                , httpExecutionContext.current());
    }
}