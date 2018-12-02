package views.matcha;

public class Matcha {
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

    public enum ButtonType {
        PRIMARY("btn-primary"),
        DANGER("btn-danger"),
        SUCCESS("btn-success"),
        WARNING("btn-warning"),
        INFO("btn-info"),
        PRIMARY_OUTLINE("btn-outline-primary"),
        DANGER_OUTLINE("btn-outline-danger"),
        SUCCESS_OUTLINE("btn-outline-success"),
        WARNING_OUTLINE("btn-outline-warning"),
        INFO_OUTLINE("btn-outline-info");
        private String value;

        ButtonType(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

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

    public enum ContainerType {
        // Define values for use of coach attributes
        CONTAINER("container"),
        CONTAINER_FLUID("container-fluid"),
        ROW("row"),
        COL("col"),
        DIV("");

        private String value;

        ContainerType(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

}
