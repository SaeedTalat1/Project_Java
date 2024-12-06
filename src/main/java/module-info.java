module com.example.main_test {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens com.example.main_test to javafx.fxml;
    exports com.example.main_test;
}