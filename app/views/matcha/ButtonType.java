package views.matcha;

public enum ButtonType {
    PRIMARY("btn-primary"),
    DANGER("btn-danger"),
    SUCCESS("btn-success"),
    WARNING("btn-warning"),
    INFO("btn-info");

    private String value;

    ButtonType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}