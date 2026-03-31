module hr.fer.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.jfree.jfreechart;
    requires java.desktop;

    opens hr.fer.projekt.application to javafx.fxml;
    exports hr.fer.projekt.application;

    opens hr.fer.projekt.controllers to javafx.fxml;
    exports hr.fer.projekt.controllers;
}
