package controllers.ApplicationComponents;

public enum Role {
    ADMIN("Admin"),
    COACH("Coach"),
    STUDENT("Student"),
    DEFAULT(STUDENT.role);

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String role() {
        return role;
    }

    public static Role getRole(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
