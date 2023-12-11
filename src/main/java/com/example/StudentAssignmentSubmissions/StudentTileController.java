package com.example.StudentAssignmentSubmissions;

import com.example.klassroom.DatabaseConnection;
import com.example.Current_Variables.current_Assignment;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentTileController {


    @FXML
    private Label Marked_or_Unmarked;
    @FXML
    private ImageView studentImageView;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label submissionDateLabel;

    @FXML
    private TextField Markstextfield;
    public void initialize(String studentUsername, String submissionDate, byte[] photoBytes) {
        // Populate the elements with student-specific data
        usernameLabel.setText( studentUsername);
        submissionDateLabel.setText( submissionDate);

        // Load the image from the byte array
        if (photoBytes != null) {
            Image image = new Image(new ByteArrayInputStream(photoBytes));
            studentImageView.setImage(image);
        }


        int existingMarks = fetchMarksFromDatabase(studentUsername, current_Assignment.current_assignment_ID);

        System.out.println("existing mark is debug / "+existingMarks);
        if (existingMarks != 0) {
            // If marks exist, update the Marks text field and mark as "Marked"
            Markstextfield.setText(String.valueOf(existingMarks));
          //  Markstextfield.setEditable(false); // Disable editing
            Marked_or_Unmarked.setText("Marked");
            Marked_or_Unmarked.setTextFill(Color.GREEN);
            Marked_or_Unmarked.setFont(Font.font(null, FontWeight.BOLD, 14));
            Marked_or_Unmarked.setText("Marked");
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
        }

        return attachmentBytes;
    }



    @FXML
    public void submitMarks() {
        // Get the marks from the text field
        String marks = Markstextfield.getText();

        // Validate the marks (you may add additional validation as needed)
        if (!marks.matches("\\d+")) {
            // Show an error message or handle invalid input
            System.out.println("Invalid marks input");
            return;
        }

        // Update the database with the marks and set Submission_Status to "marked"
        updateMarksInDatabase(usernameLabel.getText(), current_Assignment.current_assignment_ID, marks);

        // Reload the content after submitting marks
        initialize(usernameLabel.getText(), submissionDateLabel.getText(), fetchPhotoBytesFromDatabase(usernameLabel.getText(), current_Assignment.current_assignment_ID));

        // You can add additional logic here, such as updating UI, displaying a confirmation, etc.
    }

    private byte[] fetchPhotoBytesFromDatabase(String studentUsername, int assignmentID) {
        Connection connection = null;
        byte[] photoBytes = null;

        try {
            // Establish a database connection (replace with your database connection logic)
            connection = DatabaseConnection.getConnection();

            // Prepare a SQL query to fetch the photo bytes based on the parameters
            String sqlQuery = "SELECT photo FROM students WHERE student_username = ?";
            DatabaseConnection.establishConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, studentUsername);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the photo bytes from the database
                photoBytes = resultSet.getBytes("photo");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database-related errors
        }

        return photoBytes;
    }
    private void updateMarksInDatabase(String studentUsername, int assignmentID, String marks) {
        Connection connection = null;

        try {
            // Establish a database connection (replace with your database connection logic)
            connection = DatabaseConnection.getConnection();

            // Prepare a SQL query to update the marks and set Submission_Status to "marked"
            String sqlQuery = "UPDATE student_assignment_junction " +
                    "SET Marks_Obtained = ?, Submission_Status = 'Marked' " +
                    "WHERE Student_Username = ? AND Assignment_ID = ?";
            DatabaseConnection.establishConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, Integer.parseInt(marks));
            preparedStatement.setString(2, studentUsername);
            preparedStatement.setInt(3, current_Assignment.current_assignment_ID);

            System.out.println("Debug ");
            // Execute the update query
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                showConfirmationAlert("Marks Updated", "Marks have been updated successfully.");
            } else {
                System.out.println("Failed to update marks.");
                // Handle the case where the update was not successful
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database-related errors
        }
    }



    private int fetchMarksFromDatabase(String studentUsername, int assignmentID) {
        Connection connection = null;
        int marks = -1;

        try {
            // Establish a database connection (replace with your database connection logic)
            connection = DatabaseConnection.getConnection();

            // Prepare a SQL query to fetch the marks based on the parameters
            String sqlQuery = "SELECT Marks_Obtained FROM student_assignment_junction " +
                    "WHERE Student_Username = ? AND Assignment_ID = ?";
            DatabaseConnection.establishConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, studentUsername);
            preparedStatement.setInt(2, current_Assignment.current_assignment_ID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the marks from the database
                marks = resultSet.getInt("Marks_Obtained");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database-related errors
        }
        return marks;
    }

    private void showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
