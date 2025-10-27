module _3024c.heroregistry {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.base;
    requires javafx.graphics;

    opens _3024c.heroregistry to javafx.fxml;
    exports _3024c.heroregistry;
    exports _3024c.heroregistry.controllers;
    opens _3024c.heroregistry.controllers to javafx.fxml;
}