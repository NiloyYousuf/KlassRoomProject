package com.example.student;

import com.example.klassroom.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinClassroomController {

    @FXML
    private TextField classroomCode;

    @FXML
    private TextField classroomPassword;


    @FXML
    private Label errorLabel;

    // Handle the "Join Classroom" button click
    public void joinClassroom(ActionEvent event) {
        String code = classroomCode.getText();
        String password = classroomPassword.getText();
        String studentUsername = CurrentStudent.CurrentStudentUsername; // Replace with actual student username

        // Check if the classroom code and password match a classroom in your database
        boolean classroomExists = checkClassroomInDatabase(code, password);

        if (classroomExists) {
            // Check if the student is already in the classroom
            boolean isStudentInClassroom = checkStudentInClassroom(studentUsername, code);

            if (!isStudentInClassroom) {
                // Add the student to the classroom in your database
                addStudentToClassroom(studentUsername, code);

                // Optionally, you can show a success message
                errorLabel.setText("Successfully joined the classroom.");
            } else {
                errorLabel.setText("You are already in this classroom.");
            }
        } else {
            errorLabel.setText("Invalid classroom code or password.");
        }
    }

    // Implement methods to interact with your database to check and add students to classrooms.
    // Replace these methods with your actual database logic.
    private boolean checkClassroomInDatabase(String code, String password) {
        try {Connection conn = DatabaseConnection.getConnection();
            DatabaseConnection.establishConnection();
            String query = "SELECT * FROM classrooms WHERE classroom_code = ? AND classroom_password = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, code);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle the error appropriately
        }
    }


    private boolean checkStudentInClassroom(String studentUsername, String code) {
        try {Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM classroom_student_junction WHERE student_username = ? AND classroom_code = ?";
            DatabaseConnection.establishConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, studentUsername);
            preparedStatement.setString(2, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle the error appropriately
        }
    }

    private void addStudentToClassroom(String studentUsername, String code) {
        try {Connection conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO classroom_student_junction (student_username, classroom_code) VALUES (?, ?)";
            DatabaseConnection.establishConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, studentUsername);
            preparedStatement.setString(2, code);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the error appropriately
        }
    }

}
