module _3024c.heroregistry {
    requires javafx.controls;
    requires javafx.fxml;

    requires spring.beans;
    requires java.sql;

    requires jakarta.persistence;

    requires org.controlsfx.controls;
    requires javafx.base;
    requires javafx.graphics;
    requires static lombok;
    requires java.desktop;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.boot;

    opens _3024c.heroregistry to spring.core, spring.beans, spring.context, javafx.fxml;
    opens _3024c.heroregistry.controllers to javafx.fxml, spring.core, spring.beans;
    opens _3024c.heroregistry.models to org.hibernate.orm.core, spring.core, spring.beans, javafx.base;

    exports _3024c.heroregistry;
    exports _3024c.heroregistry.controllers;

}