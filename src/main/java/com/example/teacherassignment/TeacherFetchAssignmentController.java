package com.example.teacherassignment;

import com.example.Database.DatabaseConnection;
import com.example.klassroom.CurrentTeacher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TeacherFetchAssignmentController implements Initializable {
    @FXML
    private VBox assignmentsVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Fetch and display assignments for a certain teacher here
        List<Assignment> assignments = null;
        try {
            assignments = fetchAssignmentsForTeacher(CurrentTeacher.current_teacher_username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Assignment assignment : assignments) {
            loadAssignmentTile(assignment);
        }
    }

    private List<Assignment> fetchAssignmentsForTeacher(String teacherUsername) throws SQLException {
        List<Assignment> assignments = new ArrayList<>();

        // Establish a database connection (assuming you have a DatabaseConnection class)
        Connection connection = DatabaseConnection.getConnection();

        // SQL query to retrieve assignments for a teacher
        String sqlQuery = "SELECT * FROM assignments WHERE Teacher_Username = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, CurrentTeacher.current_teacher_username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Assignment assignment = new Assignment();
                assignment.setId(resultSet.getInt("Assignment_ID"));
                assignment.setAssignmentText(resultSet.getString("Assignment_Text"));
                assignment.setAssignDate(resultSet.getDate("Assign_Date"));
                assignment.setDeadline(resultSet.getDate("Deadline"));
                assignment.setAttachment(resultSet.getBytes("Attachment"));
                assignment.setMarks(resultSet.getInt("Marks"));
                assignment.setTeacherUsername(resultSet.getString("Teacher_Username"));
                assignment.setClassroomCode(resultSet.getString("Classroom_Code"));

                assignments.add(assignment);
            }

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }

    private void loadAssignmentTile(Assignment assignment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_assignment_tile.fxml"));
            HBox assignmentTile = loader.load();
            TeacherAssignmentTileController assignmentTileController = loader.getController();

            // Pass individual properties to setAssignmentDetails
            assignmentTileController.setAssignmentDetails(
                    assignment.getAssignmentText(),
                    assignment.getAssignDate().toString(),
                    assignment.getDeadline().toString(),
                    String.valueOf(assignment.getMarks()),
                    assignment.getClassroomCode(),
                    assignment.getId()
            );

            assignmentsVBox.getChildren().add(assignmentTile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
