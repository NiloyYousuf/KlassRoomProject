package com.example.StudentAssignmentSubmissions;

import com.example.klassroom.DatabaseConnection;
import com.example.klassroom.current_Assignment;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentTileController {

    @FXML
    private ImageView studentImageView;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label submissionDateLabel;

    public void initialize(String studentUsername, String submissionDate, byte[] photoBytes) {
        // Populate the elements with student-specific data
        usernameLabel.setText( studentUsername);
        submissionDateLabel.setText("Submission Date: " + submissionDate);

        // Load the image from the byte array
        if (photoBytes != null) {
            Image image = new Image(new ByteArrayInputStream(photoBytes));
            studentImageView.setImage(image);
        }
    }

    public void Download_Attachment_clicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        // Fetch the file name from the database
        String originalFilename = fetchOriginalFilename(usernameLabel.getText(), current_Assignment.current_assignment_ID);

        if (originalFilename != null) {
            fileChooser.setInitialFileName(originalFilename);
        } else {
            fileChooser.setInitialFileName("attachment.dat"); // Provide a default name if the database value is not available
        }

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            // Fetch the attachment from the database
            byte[] attachmentBytes = fetchAttachmentFromDatabase(usernameLabel.getText(), current_Assignment.current_assignment_ID);

            // If the attachment exists, save it to the specified file
            if (attachmentBytes != null) {
                try (OutputStream os = new FileOutputStream(file)) {
                    os.write(attachmentBytes);
                    System.out.println("Attachment saved successfully to " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the error
                }
            } else {
                System.out.println("Attachment not found or couldn't be retrieved.");
                // Handle the case where the attachment is not found or couldn't be retrieved.
            }
        } else {
            System.out.println("No file selected for saving.");
            // Handle the case where the user didn't select a file to save.
        }
    }

    private String fetchOriginalFilename(String studentUsername, int assignmentID) {
        // Implement your database fetching logic here to retrieve the original filename.

        Connection connection = null;
        String originalFilename = null;

        try {
            // Establish a database connection (replace with your database connection logic)
            connection = DatabaseConnection.getConnection();

            // Prepare a SQL query to fetch the original filename based on the parameters
            String sqlQuery = "SELECT Original_Filename FROM student_assignment_junction " +
                    "WHERE Student_Username = ? AND Assignment_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, usernameLabel.getText());
            preparedStatement.setInt(2, current_Assignment.current_assignment_ID);

            System.out.println("---"+usernameLabel.getText() + current_Assignment.current_assignment_ID+"----");

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the original filename from the database
                originalFilename = resultSet.getString("Original_Filename");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database-related errors
        } finally {
            // Close the database connection (replace with your database connection management)
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle database connection closing errors
                }
            }
        }

        return originalFilename;
    }

    private byte[] fetchAttachmentFromDatabase(String studentUsername, int assignmentID) {
        Connection connection = null;
        byte[] attachmentBytes = null;

        try {
            // Establish a database connection (replace with your database connection logic)
            connection = DatabaseConnection.getConnection();

            // Prepare a SQL query to fetch the attachment based on the parameters
            String sqlQuery = "SELECT Uploaded_Assignment FROM student_assignment_junction " +
                    "WHERE Student_Username = ? AND Assignment_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, usernameLabel.getText());
            preparedStatement.setInt(2, current_Assignment.current_assignment_ID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the attachment bytes from the database
                attachmentBytes = resultSet.getBytes("Uploaded_Assignment");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database-related errors
        } finally {
            // Close the database connection (replace with your database connection management)
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle database connection closing errors
                }
            }
        }

        return attachmentBytes;
    }

}
