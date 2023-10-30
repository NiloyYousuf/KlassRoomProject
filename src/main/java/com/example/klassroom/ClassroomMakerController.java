package com.example.klassroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassroomMakerController {


    @FXML
    private TextField classroomname;

    @FXML
    private TextField classroomCode;

    @FXML
    private TextField classroomPassword;



    public void createClassroom(ActionEvent event) {
        String code = classroomCode.getText();
        String password = classroomPassword.getText();
        String teacherUsername = CurrentTeacher.current_teacher_username;
        String classroomName = classroomname.getText();

        // Check if a classroom with the same code already exists
        if (classroomCodeExists(code)) {
            showAlert("Classroom with the same code already exists.");
        } else {
            try {
                Connection conn = DatabaseConnection.getConnection();
                String insertQuery = "INSERT INTO classrooms (teacher_username, classroom_code, classroom_password, subject_name) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
                preparedStatement.setString(1, teacherUsername);
                preparedStatement.setString(2, code);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, classroomName);
                preparedStatement.executeUpdate();
                conn.close();

                // Show a success message
                showAlert("Classroom created successfully.");

            } catch (SQLException e) {
                e.printStackTrace();
                // Show an error message
                showAlert("Error creating the classroom.");
            }
        }
    }

    private boolean classroomCodeExists(String code) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String selectQuery = "SELECT classroom_id FROM classrooms WHERE classroom_code = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Returns true if a classroom with the same code exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred, treat as if the classroom exists to avoid duplicates
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Classroom Creation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

