package com.example.klassroom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentAssignmentsApp extends Application {
    private Stage primaryStage;
    private VBox assignmentTilesContainer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Student Assignments");

        // Load assignment tiles based on the student's ID (e.g., "student1")
        loadAssignmentTiles(CurrentStudent.CurrentStudentUsername);

        Scene scene = new Scene(assignmentTilesContainer, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    private void loadAssignmentTiles(String studentId) {
//        assignmentTilesContainer = new VBox(10);
//
//        // Replace the following with code to retrieve assignment data for the student from the database
//        // For now, we use dummy data
//        String[] assignmentIds = {"1", "2", "3", "4", "5"};
//
//        for (String assignmentId : assignmentIds) {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("AssignmentTile.fxml"));
//                Parent assignmentTile = loader.load();
//                AssignmentTileController controller = loader.getController();
//
//                // Load assignment data into the assignment tile
//                loadAssignmentData(controller, studentId, assignmentId);
//
//                assignmentTilesContainer.getChildren().add(assignmentTile);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void loadAssignmentTiles(String studentId) {
        assignmentTilesContainer = new VBox(10);

        // Connect to the database
        try (Connection connection =DatabaseConnection.getConnection()) {
            String sql = "SELECT A.Assignment_ID, A.Assignment_Text, A.Assign_Date, A.Deadline, A.Attachment, A.Marks, A.Teacher_Username, A.Classroom_Code, " +
                    "S.Submission_Date, S.Submission_Status, S.Marks_Obtained, S.Uploaded_Assignment " +
                    "FROM student_assignment_junction S " +
                    "INNER JOIN assignments A ON S.Assignment_ID = A.Assignment_ID " +
                    "WHERE S.Student_Username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AssignmentTile.fxml"));
                Parent assignmentTile = loader.load();
                AssignmentTileController controller = loader.getController();

                // Load assignment data into the assignment tile from the database
                int assignment_ID =resultSet.getInt("Assignment_ID");
                String assignment_submission_status= resultSet.getString("Submission_Status");
                String assignment_classroomcode = resultSet.getString("Classroom_COde");
                String assignmentText = resultSet.getString("Assignment_Text");
                String assignDate = resultSet.getString("Assign_Date");
                String deadline = resultSet.getString("Deadline");

                // You can also retrieve other columns like Attachment, Marks, Teacher_Username, Classroom_Code, Submission_Date, Submission_Status, Marks_Obtained, Uploaded_Assignment, etc.
                String submissionDate = resultSet.getString("Submission_Date");
                String submissionStatus = resultSet.getString("Submission_Status");
                int marksObtained = resultSet.getInt("Marks_Obtained");
                byte[] uploadedAssignment = resultSet.getBytes("Uploaded_Assignment");

             //   controller.setAssignmentData(assignmentText, assignDate, deadline);
                controller.setAssignmentData(assignmentText, assignDate, deadline,assignment_ID,assignment_classroomcode);

              //  controller.setAssignmentData(assignmentText, assignDate, deadline, submissionDate, submissionStatus, marksObtained, uploadedAssignment);

                assignmentTilesContainer.getChildren().add(assignmentTile);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


//    private void loadAssignmentData(AssignmentTileController controller, String studentId, String assignmentId) {
//        // Replace this with code to retrieve assignment data for the student and assignment from the database
//        // For now, we set dummy data
//        controller.setAssignmentData("Assignment " + assignmentId, "2023-11-01", "2023-11-15");
//    }
}
