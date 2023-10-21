module com.example.klassroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jfreechart;
    requires java.desktop;
    exports com.example.dev2;// input by dev2

    opens com.example.dev2 to javafx.fxml;//input by dev2
    opens com.example.klassroom to javafx.fxml;
    exports com.example.klassroom;
}