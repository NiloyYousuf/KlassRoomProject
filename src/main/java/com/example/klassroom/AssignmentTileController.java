package com.example.klassroom;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssignmentTileController {
    @FXML
    private Label assignmentIdLabel;
    @FXML
    private Label assignmentTextLabel;
    @FXML
    private Label assignDateLabel;
    @FXML
    private Label deadlineLabel;


    @FXML
    private  Label classroomCode;

    @FXML
    private  Label status;

    @FXML
    private Button see_assignment;

    public void setAssignmentData(String assignmentText, String assignDate, String deadline) {
        assignmentTextLabel.setText("Assignment Text: " + assignmentText);
        assignDateLabel.setText("Assign Date: " + assignDate);
        deadlineLabel.setText("Deadline: " + deadline);

    }


    public void setAssignmentData(String assignmentText, String assignDate, String deadline, int assignment_ID,  String assignment_classroomcode) {
        assignmentTextLabel.setText("Assignment Text: " + assignmentText);
        assignDateLabel.setText("Assign Date: " + assignDate);
        deadlineLabel.setText("Deadline: " + deadline);
      assignmentIdLabel.setText(Integer.toString(assignment_ID));
      classroomCode.setText("Classroom Code: " +assignment_classroomcode);
      status.setText("Submission Status : " + getSubmissionStatus(CurrentStudent.CurrentStudentUsername,assignment_ID));



    }

    public void see_assignment_button_pressed() {
        current_Assignment.current_assignment_ID=Integer.parseInt(assignmentIdLabel.getText());

        try {
            // Load the new FXML file

            FXMLLoader loader = new FXMLLoader(new File( "src/main/resources/com/example/klassroom/studentUploadAssignment/studentUploadAssignment.fxml").toURI().toURL());
            Parent newContent = loader.load();

            // Get the controller of the new FXML (if needed)
            // StudentAssignmentDetailsController newController = loader.getController();

            // Create a new scene with the new content
            Scene newScene = new Scene(newContent);

            // Create a new stage for the assignment details
            Stage assignmentDetailsStage = new Stage();
            assignmentDetailsStage.setScene(newScene);
            assignmentDetailsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any exceptions related to loading the new FXML
        }
    }



    private String getSubmissionStatus(String studentUsername, int assignmentID) {
        // Implement a database query to get the submission status
        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseConnection.establishConnection();
            String sql = "SELECT Submission_Status FROM student_assignment_junction WHERE Student_Username = ? AND Assignment_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentUsername);
            preparedStatement.setInt(2, assignmentID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("Submission_Status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Not submitted";
    }



}
