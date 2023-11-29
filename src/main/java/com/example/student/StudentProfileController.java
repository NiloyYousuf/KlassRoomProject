package com.example.student;
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

public class StudentProfileController {
    @FXML
    private ImageView studentPhoto;
    @FXML
    private Label studentInfoLabel;


    public void initialize() {
        // Retrieve the current student ID (you may have a mechanism to get this)
        String currentStudentusername =CurrentStudent.CurrentStudentUsername;

        // Load the student information based on the current student ID
        loadStudent(currentStudentusername);
    }


    public void loadStudent(String currentStudentusername) {
        String studentUsername = currentStudentusername;

        // Connect to the database and retrieve student information
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT student_username, student_email, photo FROM students WHERE student_username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String studentName = resultSet.getString("student_username");
                String studentEmail = resultSet.getString("student_email");
                studentInfoLabel.setText("Student Name: " + studentName + "\nStudent Email: " + studentEmail);

                // Check if a photo is available
                Blob photoBlob = resultSet.getBlob("photo");
                if (photoBlob != null) {
                    // Load the image from the Blob data
                    InputStream inputStream = photoBlob.getBinaryStream();
                    Image image = new Image(inputStream);
                    studentPhoto.setImage(image);
                } else {
                    studentPhoto.setImage(null); // Clear the image if no photo is available
                }
            } else {
                studentInfoLabel.setText("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            studentInfoLabel.setText("Error loading student information.");
        }
    }

    public void uploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            // Process the uploaded image and save it to the database
            try (Connection connection = DatabaseConnection.getConnection()) {
                String studentUsername = CurrentStudent.CurrentStudentUsername; // Get the student's username
                String sql = "UPDATE students SET photo = ? WHERE student_username = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                // Convert the selected image file to a byte array
                byte[] imageBytes = Files.readAllBytes(file.toPath());

                // Set the byte array as a binary stream parameter in the prepared statement
                preparedStatement.setBytes(1, imageBytes);
                preparedStatement.setString(2, studentUsername);

                int rowsAffected = preparedStatement.executeUpdate();
                preparedStatement.close();

                if (rowsAffected > 0) {
                    // Image saved successfully
                    studentPhoto.setImage(new Image(file.toURI().toString()));
                    showSuccessAlert("Image Uploaded", "Image uploaded successfully.");
                } else {
                    // Handle database update failure, display an error message, or take appropriate action
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