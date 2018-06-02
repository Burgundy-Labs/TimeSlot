package application_components.annotations;

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/* Restricts route / method access based on current users role.
 *  Admin - Only allows admins to access method
 *  Coach - Allows both coaches and admins to access method
 *  Student - Only ensures that a user is currently signed in
 */
@With(AuthenticateAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticate {
    String role() default "Student";
}