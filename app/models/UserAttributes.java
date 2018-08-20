package models;

public enum UserAttributes{

    // Define values for use of coach attributes
    IS_COACH("isCoach");

    private String value;

    UserAttributes(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}