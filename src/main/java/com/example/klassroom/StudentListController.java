package com.example.klassroom;

import com.example.klassroom.DatabaseConnection;
import com.example.klassroom.StudentAttendance;
import com.example.klassroom.StudentAttendanceCell;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import java.util.List;
import java.util.ArrayList;

public class StudentListController implements Initializable {

    @FXML
    private ListView<StudentAttendance> studentListView;

    Connection connection = DatabaseConnection.getConnection();

   public static String classroomCode;

    @FXML
    private Button complete;

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

        try (
             PreparedStatement statement = connection.prepareStatement(query)) {

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
        String classroomCode = "C101"; // Replace with the actual classroom code

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

        // Optionally, you can show a confirmation message or perform other actions
    }

    private void updateAttendanceInDatabase(String studentUsername, LocalDate date, String classroomCode, boolean isPresent) {
        // Check if the entry exists in the attendance table
        String selectQuery = "SELECT * FROM attendance WHERE student_username = ? AND Date = ? AND classroom_code = ?";
        boolean entryExists = false;

        try {
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





}
