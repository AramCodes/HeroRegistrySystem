module _3024c.heroregistry {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens _3024c.heroregistry to javafx.fxml;
    exports _3024c.heroregistry;
}