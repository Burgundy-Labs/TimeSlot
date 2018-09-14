package views.matcha;

public enum Container {
    // Define values for use of coach attributes
    CONTAINER("container"),
    CONTAINER_FLUID("container-fluid"),
    ROW("row");

    private String value;

    Container(final String value) {
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