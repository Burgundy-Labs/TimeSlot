package controllers.ApplicationComponents;

import java.util.ArrayList;
import java.util.List;

public class Role {
    private static List<Role> roles = new ArrayList<>();
    private String role;

    public Role(String role) {
        this.role = role;
        roles.add(this);
    }

    public static List<Role> getRoles() {
        return roles;
    }

    public static Role getRole(String role) {
        for(Role r : roles) {
            if(r.role.equals(role)){
                return r;
            }
        }
        return null;
    }

    public String role() {
        return this.role;
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }
}
