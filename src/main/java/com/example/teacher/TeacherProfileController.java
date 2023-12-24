package com.example.teacher;

import com.example.klassroom.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;

public class TeacherProfileController {
    @FXML
    private ImageView teacherPhoto;
    @FXML
    private Label teacherInfoLabel;

    public void initialize() {
        // Retrieve the current teacher's username (you may have a mechanism to get this)
        String currentTeacherUsername = CurrentTeacher.current_teacher_username;

        // Load the teacher information based on the current teacher's username
        loadTeacher(currentTeacherUsername);
    }

    public void loadTeacher(String currentTeacherUsername) {
        String teacherUsername = currentTeacherUsername;

        // Connect to the database and retrieve teacher information
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT teacher_username, teacher_email, photo FROM teachers WHERE teacher_username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, teacherUsername);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String teacherName = resultSet.getString("teacher_username");
                        String teacherEmail = resultSet.getString("teacher_email");
                        teacherInfoLabel.setText("Teacher Name: " + teacherName + "\nTeacher Email: " + teacherEmail);

                        // Check if a photo is available
                        Blob photoBlob = resultSet.getBlob("photo");
                        if (photoBlob != null) {
                            // Load the image from the Blob data
                            InputStream inputStream = photoBlob.getBinaryStream();
                            Image image = new Image(inputStream);
                            teacherPhoto.setImage(image);
                        } else {
                            teacherPhoto.setImage(null); // Clear the image if no photo is available
                        }
                    } else {
                        teacherInfoLabel.setText("Teacher not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            teacherInfoLabel.setText("Error loading teacher information.");
        }
    }

    public void uploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            // Process the uploaded image and save it to the database
            try {Connection connection = DatabaseConnection.getConnection();
                String teacherUsername = CurrentTeacher.current_teacher_username; // Get the teacher's username
                String sql = "UPDATE teachers SET photo = ? WHERE teacher_username = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                    // Convert the selected image file to a byte array
                    byte[] imageBytes = Files.readAllBytes(file.toPath());

                    // Set the byte array as a binary stream parameter in the prepared statement
                    preparedStatement.setBytes(1, imageBytes);
                    preparedStatement.setString(2, teacherUsername);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Image saved successfully
                        teacherPhoto.setImage(new Image(file.toURI().toString()));
                        showSuccessAlert("Image Uploaded", "Image uploaded successfully.");
                    } else {
                        // Handle database update failure, display an error message, or take appropriate action
                    }
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                // Handle database errors or other exceptions
            }
        }
    }

    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
