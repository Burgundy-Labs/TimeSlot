package views.matcha;

public enum ColumnSize {
    // Define values for use of coach attributes
    TWELVE("col-md-12"),
    FULL("col-md-12"),
    ELEVEN("col-md-6"),
    TEN("col-md-6"),
    NINE("col-md-6"),
    EIGHT("col-md-6"),
    SEVEN("col-md-6"),
    SIX("col-md-6"),
    HALF("col-md-6"),
    FIVE("col-md-5"),
    FOUR("col-md-4"),
    THIRD("col-md-4"),
    THREE("col-md-3"),
    QUARTER("col-md-3"),
    TWO("col-md-2"),
    ONE("col-md-1");

    private String value;

    ColumnSize(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}