package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentLoginController {
    @FXML
    private Button studentloginbutton;

    @FXML
    private TextArea studentIDTextArea;

    @FXML
    private PasswordField passwordTextArea;

    @FXML
    private Text errorMessageText;

    @FXML
    private void handleStudentLoginButtonClick() {
        // Get the student ID and password from the text areas
        String student_username = studentIDTextArea.getText();
        String password = passwordTextArea.getText();

        System.out.println(StudentMenuController.student_username);

        // Fetch student information from the database using the provided DatabaseConnection class
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            try {
                // Define the SQL query to retrieve student information based on ID and password
                String sql = "SELECT * FROM students WHERE student_username = ? AND student_password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, student_username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Student login successful
                    // You can proceed to the student's dashboard or perform other actions
                    errorMessageText.setText("Login successful"); // Clear any previous error message
                    Current_User.is_student=true;
                    CurrentStudent.CurrentStudentUsername=student_username;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentFinalDashboard.fxml"));
                    Parent studentLogin = loader.load();

                    // Get the current scene and set the student login content
                    Scene currentScene = studentloginbutton.getScene();
                    currentScene.setRoot(studentLogin);

                } else {
                    // Student login failed
                    errorMessageText.setText("Invalid student ID or password");
                }

                // Close the result set and statement
                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                errorMessageText.setText("Database error");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                // Close the database connection when done
               // DatabaseConnection.closeConnection();
            }
        } else {
            errorMessageText.setText("Failed to obtain a database connection.");
        }
    }
}
