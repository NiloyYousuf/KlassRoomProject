package com.example.dev2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherRegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button signUpButton;

    public void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check email validity
        if (!isValidEmail(email)) {
            showAlert("Incorrect email format", "Please enter a valid email (e.g., abc@gmail.com).");
            return;
        }

        // Check password length
        if (password.length() < 8) {
            showAlert("Password length error", "Password must be at least 8 characters long.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showAlert("Password mismatch", "Password and confirm password do not match.");
            return;
        }
        if (emailExists(email)) {
            showAlert("Email already in use", "This email has already been used.");
            return;
        }
        // Add database insertion logic for teachers
        if (insertDataToDatabase(username, email, password)) {
            showAlert("Success", "Registration successful!");
        } else {
            showAlert("Database Error", "Failed to insert data into the database.");
        }

        // Clear input fields
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    public void handleBackToRegistration(ActionEvent event) {
        // Handle the "Back to Registration" button action here
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        stage.close();

        // Start the RegisterMain window
        RegisterMain registerMain = new RegisterMain();
        try {
            registerMain.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        // Simple email validation: Check for @gmail.com at the end
        return email.matches(".+@gmail\\.com$") && !email.contains(" ");
    }

    private boolean emailExists(String email) {
        try {
            Connection connection = getConnection();
            String query = "SELECT COUNT(*) FROM teachers WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            connection.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean insertDataToDatabase(String username, String email, String password) {
        try {
            Connection connection = getConnection();
            String insertQuery = "INSERT INTO teachers (teacher_username, teacher_email, teacher_password) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int rowsAffected = preparedStatement.executeUpdate();
            connection.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/Project";
        String username = "root";
        String password = "200041116";
        return DriverManager.getConnection(url, username, password);
    }
}
