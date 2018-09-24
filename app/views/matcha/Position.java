package views.matcha;

public enum Position {
    // Define values for use of coach attributes
    TOP("top"),
    RIGHT("right"),
    BOTTOM("bottom"),
    LEFT("left");
    private String value;

    Position(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}