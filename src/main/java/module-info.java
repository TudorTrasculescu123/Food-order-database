module ro.tucn.assignment3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.logging;
    requires java.sql;
    requires java.desktop;
    requires org.postgresql.jdbc;

    opens ro.tucn.assignment3 to javafx.fxml;
    exports ro.tucn.assignment3;
    exports ro.tucn.assignment3.Controller;
    opens ro.tucn.assignment3.Controller to javafx.fxml;
    exports ro.tucn.assignment3.Model;
    opens ro.tucn.assignment3.Model to javafx.fxml;
    exports ro.tucn.assignment3.BusinessLogic;
    opens ro.tucn.assignment3.BusinessLogic to javafx.fxml;
    exports ro.tucn.assignment3.Connection;
    opens ro.tucn.assignment3.Connection to javafx.fxml;
    exports ro.tucn.assignment3.DataAccess;
    opens ro.tucn.assignment3.DataAccess to javafx.fxml;
    exports ro.tucn.assignment3.BusinessLogic.Validators;
    opens ro.tucn.assignment3.BusinessLogic.Validators to javafx.fxml;
}