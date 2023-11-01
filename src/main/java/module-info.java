module com.example.klassroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jfreechart;
    requires java.desktop;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    exports com.example.Registration;// input by dev2
    exports com.example.studentUploadAssignment;
    exports com.example.StudentAssignmentSubmissions;
    exports com.example.teacherassignment;
    opens com.example.Registration to javafx.fxml;//input by dev2
    opens com.example.StudentAssignmentSubmissions to javafx.fxml;
    opens com.example.klassroom to javafx.fxml;
    opens com.example.teacherassignment to javafx.fxml;
    opens com.example.studentUploadAssignment to javafx.fxml;
    exports com.example.klassroom;
    opens com.example to javafx.fxml;
    exports com.example.Notification;
    opens com.example.Notification to javafx.fxml;
    exports com.example.Login;
    opens com.example.Login to javafx.fxml;
    exports com.example.teacher;
    opens com.example.teacher to javafx.fxml;
    exports com.example.student;
    opens com.example.student to javafx.fxml;
    exports com.example.Current_Variables;
    opens com.example.Current_Variables to javafx.fxml;
    exports com.example.Comment;
    opens com.example.Comment to javafx.fxml;
    exports com.example;
}