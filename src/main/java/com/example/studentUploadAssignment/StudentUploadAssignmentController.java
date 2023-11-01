package com.example.studentUploadAssignment;

import com.example.student.CurrentStudent;
import com.example.klassroom.DatabaseConnection;
import com.example.Current_Variables.current_Assignment;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentUploadAssignmentController {

    @FXML
    private Label statusLabel;


    @FXML
    private Label assignmentTextLabel; // Label to display Assignment Text
    @FXML
    private Label assignDateLabel;   // Label to display Assign Date
    @FXML
    private Label deadlineLabel;     // Label to display Deadline
    @FXML
    private Label teacherUsernameLabel; // Label to display Teacher's Username
    @FXML
    private Label classroomCodeLabel;  // Label to display Classroom Code

    @FXML
    private Label attachmentStatusLabel; // Label to display Attachment Status

    private byte[] attachmentData;

    private String originalFileName;

    @FXML
    private Button Download;

    @FXML
    private Label uploadedFileNameLabel;

    @FXML
    public void initialize() {
        // Load and display assignment details when the view is initialized
        fetchAssignmentDetailsAndPopulateLabels();
        fetchAttachment();
        fetchUploadedFileName();

        if (attachmentData != null) {
            attachmentStatusLabel.setText("Attachment Available");
        } else {
            attachmentStatusLabel.setText("No attachment available.");
            Download.setDisable(true);
        }
    }

    @FXML
    public void uploadAssignment() {
        // Check if the student has already submitted the assignment
        if (hasStudentSubmittedAssignment()) {
            statusLabel.setText("You have already submitted the assignment. To submit again, please unsubmit first.");
            return;
        }

        // Open a file dialog for the user to select an assignment file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Assignment File");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            String username = CurrentStudent.CurrentStudentUsername; // Replace with the actual username
            int assignmentID = current_Assignment.current_assignment_ID; // Replace with the actual assignment ID
            String submissionStatus = "Submitted";
            String originalFileName = selectedFile.getName(); // Get the original file name

            // Get the current date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String submissionDate = sdf.format(new Date());

            // Update the status label
            statusLabel.setText("Uploading...");

            // Upload the assignment to the database
            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO student_assignment_junction (Assignment_ID, Student_Username, Submission_Date, Submission_Status, Original_Filename, Uploaded_Assignment) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, assignmentID);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, submissionDate);
                preparedStatement.setString(4, submissionStatus);
                preparedStatement.setString(5, originalFileName);
                preparedStatement.setBytes(6, Files.readAllBytes(selectedFile.toPath()));

                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    statusLabel.setText("Assignment uploaded successfully.");
                } else {
                    statusLabel.setText("Failed to upload assignment.");
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                statusLabel.setText("Error: " + e.getMessage());
            }
        }
    }

    private boolean hasStudentSubmittedAssignment() {
        int assignmentID = current_Assignment.current_assignment_ID;
        String username = CurrentStudent.CurrentStudentUsername;

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM student_assignment_junction WHERE Assignment_ID = ? AND Student_Username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, assignmentID);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int submissionCount = resultSet.getInt(1);
                return submissionCount > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    private void fetchAssignmentDetailsAndPopulateLabels() {
        int assignmentID = current_Assignment.current_assignment_ID;
        String studentUsername = CurrentStudent.CurrentStudentUsername;

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Fetch assignment details from the assignments table
            String assignmentDetailsSQL = "SELECT Assignment_Text, Assign_Date, Deadline, Teacher_Username, Classroom_Code FROM assignments WHERE Assignment_ID = ?";
            PreparedStatement assignmentDetailsStatement = connection.prepareStatement(assignmentDetailsSQL);
            assignmentDetailsStatement.setInt(1, assignmentID);
            ResultSet assignmentDetailsResult = assignmentDetailsStatement.executeQuery();

            if (assignmentDetailsResult.next()) {
                assignmentTextLabel.setText("Assignment Text: " + assignmentDetailsResult.getString("Assignment_Text"));
                assignDateLabel.setText("Assign Date: " + assignmentDetailsResult.getDate("Assign_Date").toString());
                deadlineLabel.setText("Deadline: " + assignmentDetailsResult.getDate("Deadline").toString());
                teacherUsernameLabel.setText("Teacher's Username: " + assignmentDetailsResult.getString("Teacher_Username"));
                classroomCodeLabel.setText("Classroom Code: " + assignmentDetailsResult.getString("Classroom_Code"));
            }

            // Fetch submission details from the student_assignment_junction table
            String submissionDetailsSQL = "SELECT Submission_Status, Marks_Obtained FROM student_assignment_junction WHERE Assignment_ID = ? AND Student_Username = ?";
            PreparedStatement submissionDetailsStatement = connection.prepareStatement(submissionDetailsSQL);
            submissionDetailsStatement.setInt(1, assignmentID);
            submissionDetailsStatement.setString(2, studentUsername);
            ResultSet submissionDetailsResult = submissionDetailsStatement.executeQuery();

            if (submissionDetailsResult.next()) {
                String submissionStatus = submissionDetailsResult.getString("Submission_Status");
                int marksObtained = submissionDetailsResult.getInt("Marks_Obtained");

                // Update the status label
                if ("Submitted".equals(submissionStatus)) {
                    statusLabel.setText("Assignment Submitted");
                } else if ("Marked".equals(submissionStatus)) {
                    statusLabel.setText("Assignment Marked - Marks: " + marksObtained);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
        }
    }




    @FXML
    public void downloadAttachment() {
        if (attachmentData != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Attachment");
            fileChooser.setInitialFileName(originalFileName); // Set the initial file name

            File savedFile = fileChooser.showSaveDialog(new Stage());
            if (savedFile != null) {
                try (OutputStream os = new FileOutputStream(savedFile)) {
                    os.write(attachmentData);
                    attachmentStatusLabel.setText("Attachment downloaded successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    attachmentStatusLabel.setText("Error: " + e.getMessage());
                }
            }
        } else {
            attachmentStatusLabel.setText("No attachment available.");
        }
    }

    private void fetchAttachment() {
        int assignmentID = current_Assignment.current_assignment_ID;

        try {
            DatabaseConnection.establishConnection();
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT Attachment, original_filename FROM assignments WHERE Assignment_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, assignmentID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                attachmentData = resultSet.getBytes("Attachment");
                if (attachmentData != null) {
                    attachmentStatusLabel.setText("Attachment Available");
                    originalFileName = resultSet.getString("original_filename");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            attachmentStatusLabel.setText("Error: " + e.getMessage());
        }
    }


    @FXML
    public void unsubmitAssignment() {
        // Show a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsubmit Confirmation");
        alert.setHeaderText("Are you sure you want to unsubmit this assignment?");
        alert.setContentText("This action cannot be undone.");

        // Customize the buttons
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);

        // Show and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // User clicked YES, proceed with unsubmitting the assignment
                int assignmentID = current_Assignment.current_assignment_ID;
                String studentUsername = CurrentStudent.CurrentStudentUsername;

                try (Connection connection = DatabaseConnection.getConnection()) {
                    String sql = "DELETE FROM student_assignment_junction WHERE Assignment_ID = ? AND Student_Username = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, assignmentID);
                    preparedStatement.setString(2, studentUsername);

                    int rows = preparedStatement.executeUpdate();
                    if (rows > 0) {
                        statusLabel.setText("Assignment unsubmitted successfully.");
                        fetchUploadedFileName(); // Refresh the uploaded file name label
                    } else {
                        statusLabel.setText("Failed to unsubmit assignment.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    statusLabel.setText("Error: " + e.getMessage());
                }
            }
            // If NO or CANCEL, do nothing
        });
    }

    private void fetchUploadedFileName() {
        int assignmentID = current_Assignment.current_assignment_ID;
        String studentUsername = CurrentStudent.CurrentStudentUsername;

       // System.out.println("Debug--" + assignmentID + studentUsername);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT Original_Filename FROM student_assignment_junction WHERE Assignment_ID = ? AND Student_Username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, assignmentID);
            preparedStatement.setString(2, studentUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String uploadedFileName = resultSet.getString("Original_Filename");
                uploadedFileNameLabel.setText("Name of File Uploaded: " + uploadedFileName);
            } else {
                uploadedFileNameLabel.setText("No file uploaded.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

}