package com.example.klassroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClassroomMakerController {

    @FXML
    private TextField classroomCode;

    @FXML
    private TextField classroomPassword;

    // Handle the "Create Classroom" button click
    public void createClassroom(ActionEvent event) {
        String code = classroomCode.getText();
        String password = classroomPassword.getText();
        String teacherUsername = CurrentTeacher.current_teacher_username; // You can change this as needed

        // Insert the classroom entry into the database (assuming you have a database connection)
        // Replace the following code with your database logic
        // Make sure you have a database connection and prepared statements
        // to safely insert data into the 'classrooms' table.
        // Example pseudo-code:

        try {
            Connection conn = DatabaseConnection.getConnection();
            String insertQuery = "INSERT INTO classrooms (teacher_username, classroom_code, classroom_password,subject_name) VALUES (?, ?, ?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, teacherUsername);
            preparedStatement.setString(2, code);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4,"CSE Subject 123");
            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Optionally, you can display a success message or handle errors here
    }
}

