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
            return CompletableFuture.supplyAsync(() -> ok(views.html.pages.login.render()));
        }
        /* Else ensure logged in user has proper role */
        String role = configuration.role();
        switch(role) {
            case "Admin":
            case "Coach":
            case "Student":
                /* If Student (or blank Authenticate annotation) allow access - ensures logged in user only */
                return authenticateRole(role, ctx);
            default:
                /* If unknown role entered - default to forbidden */
                return CompletableFuture.supplyAsync(() -> forbidden(views.html.pages.error_pages.unauthorized.render()));
        }
    }

    /* Checks session role against annotation target and allows execution if matched up */
    private CompletionStage<Result> authenticateRole(String role, Http.Context ctx) {
        /* Admin as access to all authenticate checks */
        if(ctx.session().get("currentRole") != null && ctx.session().get("currentRole").equals("Admin")){
            return delegate.call(ctx);
        }
        /* Student role is accepted as long as a currentRole exists in the session */
        else if( ctx.session().get("currentRole") != null && role.equals("Student")){
            return delegate.call(ctx);
        }
        /* Finally, if the role isn't matched - check that the provided authenticate matches the session role */
        else if(ctx.session().get("currentRole") != null && ctx.session().get("currentRole").equalsIgnoreCase(role)) {
            return delegate.call(ctx);
        }
        /* If any are failed to return delegate call - redirect to forbidden */
        return CompletableFuture.supplyAsync(() -> forbidden(views.html.pages.error_pages.unauthorized.render()));
    }
}
