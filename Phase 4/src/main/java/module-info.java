module _3024c.heroregistry {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;             // JDBC API
    requires mysql.connector.j;

    requires org.controlsfx.controls;
    requires javafx.base;
    requires javafx.graphics;
    requires static lombok;
    requires java.desktop;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.boot;

    opens _3024c.heroregistry to javafx.fxml;
    exports _3024c.heroregistry;
    exports _3024c.heroregistry.controllers;
    opens _3024c.heroregistry.models to javafx.base;
    opens _3024c.heroregistry.controllers to javafx.fxml;
}