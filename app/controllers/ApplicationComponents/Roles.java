package controllers.ApplicationComponents;

import java.util.ArrayList;
import java.util.List;

public class Roles {
    private static List<Roles> roles = new ArrayList<>();
    private String role;

    public Roles(String role) {
        this.role = role;
        roles.add(this);
    }

    public static List<Roles> getRoles() {
        return roles;
    }

    public static Roles getRole(String role) {
        for(Roles r : roles) {
            if(r.role.equals(role)){
                return r;
            }
        }
        return null;
    }

    public String role() {
        return this.role;
    }

    public void removeRole(Roles role) {
        roles.remove(role);
    }
}
