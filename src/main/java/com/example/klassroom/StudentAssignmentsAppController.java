package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentAssignmentsAppController {

    @FXML
    private VBox    assignmentTilesContainer ;




    public void initialize() {
        // Load assignment tiles based on the student's ID (e.g., "student1")
        loadAssignmentTiles();
    }


    private void loadAssignmentTiles() {
        // Create a list to store classroom codes for the student
        List<String> classroomCodes = new ArrayList<>();

        // Connect to the database and retrieve classroom codes for the student
        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseConnection.establishConnection();
            String classroomQuery = "SELECT classroom_code FROM classroom_student_junction WHERE student_username = ?";
            PreparedStatement classroomStatement = connection.prepareStatement(classroomQuery);
            classroomStatement.setString(1, CurrentStudent.CurrentStudentUsername);
            ResultSet classroomResultSet = classroomStatement.executeQuery();

            while (classroomResultSet.next()) {
                classroomCodes.add(classroomResultSet.getString("classroom_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database-related exceptions
        }

        // Now, you have a list of classroom codes that the student is enrolled in
        // Use this list to fetch assignments for those classrooms
        for (String classroomCode : classroomCodes) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                DatabaseConnection.establishConnection();
                String sql = "SELECT Assignment_ID, Assignment_Text, Assign_Date, Deadline, Attachment, Marks, Teacher_Username, Classroom_Code FROM assignments WHERE Classroom_Code = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, classroomCode);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AssignmentTile.fxml"));
                    Parent assignmentTile = loader.load();
                    AssignmentTileController controller = loader.getController();

                    int assignment_ID = resultSet.getInt("Assignment_ID");
                    String assignmentText = resultSet.getString("Assignment_Text");
                    String assignDate = resultSet.getString("Assign_Date");
                    String deadline = resultSet.getString("Deadline");

                    controller.setAssignmentData(assignmentText, assignDate, deadline, assignment_ID, classroomCode);

                    assignmentTilesContainer.getChildren().add(assignmentTile);
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                // Handle any database-related exceptions
            }
        }
    }


}
