package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDate;

public class AssignmentDetailsController {
    @FXML
    private Label assignmentTextLabel;
    @FXML
    private Label assignDateLabel;
    @FXML
    private Label deadlineLabel;
    @FXML
    private Label teacherLabel;
    @FXML
    private TextArea submissionText;

    private File selectedAssignmentFile;
    byte[] uploadedAssignmentData = null;

    public void uploadAssignment() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            selectedAssignmentFile = file;

            try {
                uploadedAssignmentData = Files.readAllBytes(selectedAssignmentFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file read error
            }
        }
    }

    public void saveAssignment() {
        String submissionTextValue = submissionText.getText();
        LocalDate submissionDate = LocalDate.now(); // Use the current date

        if (submissionTextValue != null && !submissionTextValue.isEmpty()) {
            try {
                Connection connection = DatabaseConnection.getConnection();
                String insertQuery = "INSERT INTO student_assignment_junction (Assignment_ID, Student_Username, Submission_Date, Submission_Status, Marks_Obtained, Uploaded_Assignment) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                preparedStatement.setInt(1,current_Assignment.current_assignment_ID); // Provide the assignment ID
                preparedStatement.setString(2, CurrentStudent.CurrentStudentUsername); // Use the current student's username
                preparedStatement.setDate(3, Date.valueOf(submissionDate));
                preparedStatement.setString(4, "Submitted"); // You can set a default submission status
                preparedStatement.setInt(5, 0); // You can set an initial marks obtained (e.g., 0)

                if (uploadedAssignmentData != null) {
                    preparedStatement.setBytes(6, uploadedAssignmentData);
                } else {
                    preparedStatement.setNull(6, Types.BLOB);
                }

                int rowsAffected = preparedStatement.executeUpdate();
                preparedStatement.close();
                connection.close();

                if (rowsAffected > 0) {
                    // Assignment submitted successfully, display a success message or perform any other actions
                    closeForm();
                } else {
                    // Handle database insert failure, display an error message, or take appropriate action
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database errors
            }
        } else {
            // Display an error message if the submission text is empty
            // You can implement error handling here
        }
    }

    private void closeForm() {
        Stage stage = (Stage) assignmentTextLabel.getScene().getWindow();
        stage.close();
    }
}
