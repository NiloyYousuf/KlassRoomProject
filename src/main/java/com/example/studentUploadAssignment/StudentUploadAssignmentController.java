package com.example.studentUploadAssignment;

import com.example.klassroom.CurrentStudent;
import com.example.klassroom.DatabaseConnection;
import com.example.klassroom.current_Assignment;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    public void initialize() {
        // Load and display assignment details when the view is initialized
        fetchAssignmentDetailsAndPopulateLabels();
        fetchAttachment();
    }

    @FXML
    public void uploadAssignment() {
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


    private void fetchAssignmentDetailsAndPopulateLabels() {
        int assignmentID = current_Assignment.current_assignment_ID;
        //int assignmentID =7;
        System.out.println(assignmentID);
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT Assignment_Text, Assign_Date, Deadline, Teacher_Username, Classroom_Code FROM assignments WHERE Assignment_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, assignmentID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                assignmentTextLabel.setText("Assignment Text: " + resultSet.getString("Assignment_Text"));
                assignDateLabel.setText("Assign Date: " + resultSet.getDate("Assign_Date").toString());
                deadlineLabel.setText("Deadline: " + resultSet.getDate("Deadline").toString());
                teacherUsernameLabel.setText("Teacher's Username: " + resultSet.getString("Teacher_Username"));
                classroomCodeLabel.setText("Classroom Code: " + resultSet.getString("Classroom_Code"));
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
}