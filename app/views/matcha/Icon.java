package views.matcha;

public enum Icon {
    // Define values for use of coach attributes
    QUESTION_MARK("fas fa-question-circle"),
    INFO("fas fa-info-circle");
    private String value;

    Icon(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}