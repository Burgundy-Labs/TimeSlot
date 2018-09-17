package views.matcha;

public enum ComponentType {
    // Define values for use of coach attributes
    BUTTON("btn");

    private String value;

    ComponentType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}