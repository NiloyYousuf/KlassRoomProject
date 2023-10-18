package com.example.klassroom;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassroomAttendanceController {

    @FXML
    private TableView<StudentAttendance> studentTable;

    @FXML
    private TableColumn<StudentAttendance, String> studentNameColumn;

    @FXML
    private TableColumn<StudentAttendance, Boolean> markAttendanceColumn;

    @FXML
    private TextField classroomCodeInput;

    @FXML
    private Button loadStudentsButton;

    public void initialize() {
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        markAttendanceColumn.setCellValueFactory(cellData -> cellData.getValue().attendanceProperty());
    }

    @FXML
    private void markAttendance(ActionEvent event) {
        // Handle marking attendance here and update the StudentAttendance objects
        // You can access the TableView's items to get the data
        // For example:
        for (StudentAttendance student : studentTable.getItems()) {
            System.out.println("Student: " + student.getStudentName() + ", Attendance: " + student.isAttendance());
        }
    }

    @FXML
    private void loadStudentsByClassroom(ActionEvent event) {
        String classroomCode = classroomCodeInput.getText();

        // Load students from the database based on the classroomCode
        List<StudentAttendance> students = fetchStudentsByClassroom(classroomCode);

        if (students != null) {
            studentTable.getItems().setAll(students);
        } else {
            // Handle the case where no students were found for the given classroom code
            System.out.println("No students found for classroom code: " + classroomCode);
        }
    }

    // Simulate loading students from the database based on classroom code
    private List<StudentAttendance> fetchStudentsByClassroom(String classroomCode) {
        // Replace this with your database query to fetch students for the given classroom code
        // Example:
        if ("C101".equals(classroomCode)) {
            List<StudentAttendance> students = new ArrayList<>();
            students.add(new StudentAttendance("Student1", false));
            students.add(new StudentAttendance("Student2", false));
            students.add(new StudentAttendance("Student3", false));
            return students;
        } else {
            return null; // Return null if classroom code is not found
        }
    }





    public static class StudentAttendance {
        private final SimpleStringProperty studentName;
        private final SimpleBooleanProperty attendance;

        public StudentAttendance(String studentName, boolean attendance) {
            this.studentName = new SimpleStringProperty(studentName);
            this.attendance = new SimpleBooleanProperty(attendance);
        }

        public String getStudentName() {
            return studentName.get();
        }

        public SimpleStringProperty studentNameProperty() {
            return studentName;
        }

        public boolean isAttendance() {
            return attendance.get();
        }

        public SimpleBooleanProperty attendanceProperty() {
            return attendance;
        }

        public void setAttendance(boolean attendance) {
            this.attendance.set(attendance);
        }

    }


    public void Go_Back()
    {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("classroomTeacher.fxml"));
            Parent root = loader.load();

            // Get the current stage (assuming you have a reference to the current stage)
            Stage stage = (Stage)studentTable.getScene().getWindow();
            // Set the new FXML content on the current stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
