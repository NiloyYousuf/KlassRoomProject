module com.example.klassroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jfreechart;
    requires java.desktop;


    opens com.example.klassroom to javafx.fxml;
    exports com.example.klassroom;
}