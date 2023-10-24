package com.example.StudentAssignmentSubmissions;

import com.example.klassroom.DatabaseConnection;
import com.example.klassroom.current_Assignment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class StudentsSubmissionController implements Initializable {

    @FXML
    private TilePane tilePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This method is automatically called when the FXML is loaded

        // Call the displayAssignmentInfo method to populate the tiles
        displayAssignmentInfo();
    }

    private void displayAssignmentInfo() {
        String assignmentId = String.valueOf(current_Assignment.current_assignment_ID);

        tilePane.getChildren().clear(); // Clear existing tiles

        try {
            // Replace with your database connection details
            Connection connection = DatabaseConnection.getConnection();

            // SQL query to fetch student info and assignment data
            String sqlQuery = "SELECT saj.Student_Username, saj.Submission_Date, s.photo " +
                    "FROM student_assignment_junction saj " +
                    "INNER JOIN students s ON saj.Student_Username = s.student_username " +
                    "WHERE saj.Assignment_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, assignmentId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String studentUsername = resultSet.getString("Student_Username");
                String submissionDate = resultSet.getString("Submission_Date");
                byte[] photoBytes = resultSet.getBytes("photo");

                // Load the student tile FXML for each student
                FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentTile.fxml"));
                VBox studentTile = loader.load();

                // Get the controller for the student tile
                StudentTileController tileController = loader.getController();

                // Initialize the student tile controller with data
                tileController.initialize(studentUsername, submissionDate, photoBytes);

                // Add the populated student tile to the TilePane
                tilePane.getChildren().add(studentTile);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
