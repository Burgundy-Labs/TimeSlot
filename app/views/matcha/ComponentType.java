package views.matcha;

public enum ComponentType {
    // Define values for use of coach attributes
    CONTAINER("container"),
    CONTAINER_FLUID("container-fluid"),
    ROW("row"),
    COL("col"),
    DIV("");

    private String value;

    ComponentType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}