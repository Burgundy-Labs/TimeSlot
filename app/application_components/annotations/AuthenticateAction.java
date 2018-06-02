package application_components.annotations;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/* Redirects to forbidden error based on session user's role */
public class AuthenticateAction extends Action<Authenticate> {
    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        /* Check to ensure user role exists (signed in) */
        if(ctx.session().get("currentRole") == null) {
            /* If user is not signed in - redirect to login */
            return CompletableFuture.supplyAsync(() -> ok(views.html.login.render()));
        }
        /* Else ensure logged in user has proper role */
        String role = configuration.role();
        switch(role) {
            case "Admin":
            case "Coach":
                return authenticateRole(role, ctx);
            case "Student":
                /* If Student (or blank Authenticate annotation) allow access - ensures logged in user only */
                return delegate.call(ctx);
            default:
                /* If unknown role entered - default to forbidden */
                return CompletableFuture.supplyAsync(() -> forbidden(views.html.error_pages.unauthorized.render()));
        }
    }

    /* Checks session role against annotation target and allows execution if matched up */
    private CompletionStage<Result> authenticateRole(String role, Http.Context ctx) {
        /* Admin is assumed to have all permissions as Coach */
        if(role.equals("Coach") && ctx.session().get("currentRole") != null && ctx.session().get("currentRole").equals("Admin")){
            return delegate.call(ctx);
        }
        else if(ctx.session().get("currentRole") == null || !ctx.session().get("currentRole").equalsIgnoreCase(role)) {
            return CompletableFuture.supplyAsync(() -> forbidden(views.html.error_pages.unauthorized.render()));
        }
        return delegate.call(ctx);
    }
}
