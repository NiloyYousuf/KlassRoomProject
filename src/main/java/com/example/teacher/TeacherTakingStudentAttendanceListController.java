package com.example.teacher;

import com.example.klassroom.*;
import com.example.Current_Variables.CurrentClassroom;
import com.example.student.StudentAttendance;
import com.example.student.StudentAttendanceCell;
import com.example.student.StudentsMonthlyAttendanceChart;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;

public class TeacherTakingStudentAttendanceListController implements Initializable {

    @FXML
    private ListView<StudentAttendance> studentListView;

    Connection connection = DatabaseConnection.getConnection();

   public static String classroomCode;

    @FXML
    private Button complete;

    @FXML
    private Button generate_report;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Replace with the actual classroom code
        populateStudentList(classroomCode);
    }

    private void populateStudentList(String classroomCode) {
        // Fetch students and their attendance status from the database
        // You should return a list of StudentAttendance objects based on your database schema
        List<StudentAttendance> studentAttendances = getStudentAttendancesFromDatabase(classroomCode);
        studentListView.getItems().addAll(studentAttendances);
        // Set the custom cell factory for the ListView
        studentListView.setCellFactory(param -> new StudentAttendanceCell());
    }


    private List<StudentAttendance> getStudentAttendancesFromDatabase(String classroomCode) {
        List<StudentAttendance> studentAttendances = new ArrayList<>();

        // Define your SQL query to fetch student usernames from the junction table
        String query = "SELECT student_username FROM classroom_student_junction WHERE classroom_code = ?";

        try {
             PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, classroomCode);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Retrieve student usernames from the result set
                String studentUsername = resultSet.getString("student_username");
                // Create a StudentAttendance object with the current date and isPresent set to true
                StudentAttendance studentAttendance = new StudentAttendance(studentUsername, LocalDate.now(), classroomCode, true);
                studentAttendances.add(studentAttendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentAttendances;
    }


    @FXML
    private void complete_button_clicked() throws SQLException {
        String classroomCode = CurrentClassroom.classroomCode; // Replace with the actual classroom code

        // Get the items (StudentAttendance objects) from the ListView
        ObservableList<StudentAttendance> studentAttendances = studentListView.getItems();

        // Iterate through the list and update the attendance in the database
        for (StudentAttendance studentAttendance : studentAttendances) {
            String studentUsername = studentAttendance.getStudentUsername();
            LocalDate date = studentAttendance.getDate();
            boolean isPresent = studentAttendance.isPresent();
            // Update the attendance in the database (you should implement this part)
            updateAttendanceInDatabase(studentUsername, date, classroomCode, isPresent);
        }

        // Show a confirmation message
        showConfirmationMessage("Attendance updated successfully.");
    }

    private void showConfirmationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Customize the button in the alert
        alert.getButtonTypes().setAll(ButtonType.OK);

        // Show the confirmation alert and wait for user response
        alert.showAndWait();
    }

    private void updateAttendanceInDatabase(String studentUsername, LocalDate date, String classroomCode, boolean isPresent) {
        // Check if the entry exists in the attendance table
        String selectQuery = "SELECT * FROM attendance WHERE student_username = ? AND Date = ? AND classroom_code = ?";
        boolean entryExists = false;

        try {
            DatabaseConnection.establishConnection();
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, studentUsername);
            selectStatement.setDate(2, java.sql.Date.valueOf(date));
            selectStatement.setString(3, classroomCode);

            ResultSet resultSet = selectStatement.executeQuery();
            entryExists = resultSet.next(); // Check if any rows are returned
        } catch (SQLException e) {
            e.printStackTrace();
            return; // Don't proceed with the update if an exception occurs
        }

        // If the entry doesn't exist, insert a new record; otherwise, update the existing record
        String sql;
        if (!entryExists) {
            sql = "INSERT INTO attendance ( is_present,student_username, Date, classroom_code) VALUES (?, ?, ?, ?)";
        } else {
            sql = "UPDATE attendance SET is_present = ? WHERE student_username = ? AND Date = ? AND classroom_code = ?";
        }

        try {

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, isPresent);
            preparedStatement.setString(2, studentUsername);
            preparedStatement.setDate(3, java.sql.Date.valueOf(date));
            preparedStatement.setString(4, classroomCode);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Attendance updated successfully.");
            } else {
                System.out.println("Attendance update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Go_Back() throws IOException {

        GlobalFxmlString.FXML_to_load = "src/main/resources/com/example/klassroom/classroomTeacher.fxml";
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/TeacherFinalDashboard.fxml").toURL());
        // Get the current stage (assuming you have a reference to the current stage)

        Parent post = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene =generate_report.getScene();
        currentScene.setRoot(post);
    }

    public void generate_report_button_clicked() {
        String classroomCode = CurrentClassroom.classroomCode;
        // Create the chart for the current year and month
        StudentsMonthlyAttendanceChart chart = new StudentsMonthlyAttendanceChart(classroomCode);
        chart.generateMonthlyAttendanceChart();
    }

    @FXML
    private void generateCSVReportButtonClicked() {
        String classroomCode = CurrentClassroom.classroomCode;
        CSVReportGenerator.generateCSVReport(classroomCode, (Stage) generate_report.getScene().getWindow());
    }



}
