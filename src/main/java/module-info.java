module niss.employeeJBC {
    requires transitive javafx.controls;
    requires javafx.base;
    requires javafx.fxml;

    requires core.fx;
    requires transitive core.db;
    requires javafx.graphics;

    opens dev.niss to javafx.fxml;
    opens dev.niss.app to javafx.fxml;

    exports dev.niss;
    exports dev.niss.app;
}
