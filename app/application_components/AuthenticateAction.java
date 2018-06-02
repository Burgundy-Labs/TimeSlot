package application_components;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/* Redirects to forbidden error based on session user's role */
public class AuthenticateAction extends Action<Authenticate> {
    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        String role = configuration.role();
        switch(role) {
            case "Admin":
            case "Coach":
                return authenticateRole(role, ctx);
            case "Student":
            default:
                return delegate.call(ctx);
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
