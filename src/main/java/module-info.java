module com.example.klassroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jfreechart;
    requires java.desktop;
    exports com.example.dev2;// input by dev2
    exports com.example.studentUploadAssignment;
    exports com.example.StudentAssignmentSubmissions;
    exports com.example.teacherassignment;
    opens com.example.dev2 to javafx.fxml;//input by dev2
    opens com.example.StudentAssignmentSubmissions to javafx.fxml;
    opens com.example.klassroom to javafx.fxml;
    opens com.example.teacherassignment to javafx.fxml;
    opens com.example.studentUploadAssignment to javafx.fxml;
    exports com.example.klassroom;
    opens com.example to javafx.fxml;
}