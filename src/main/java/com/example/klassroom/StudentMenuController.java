package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMenuController {
    @FXML
    private ScrollPane classroomScrollPane;

    @FXML
    private TilePane classroomTilePane;

    // The student's username; you should set this value when the student logs in
    public static String student_username;

    @FXML
    public void initialize() {
        // Load and populate the classroom tiles for the logged-in student
        loadClassroomTiles();
    }

    // This method loads and populates the classroom tiles
    private void loadClassroomTiles() {
        // Clear any existing tiles
        classroomTilePane.getChildren().clear();

        // Fetch classroom data from the database based on the student's username
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            try {
                // Define the SQL query to retrieve classroom information for the student
                String sql = "SELECT c.classroom_code, c.subject_name, c.classroom_id " +
                        "FROM classrooms c " +
                        "INNER JOIN classroom_student_junction j " +
                        "ON c.classroom_code = j.classroom_code " +
                        "WHERE j.student_username = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, student_username);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    // Load the custom tile FXML for each classroom
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassroomTileStudent.fxml"));
                    try {
                        // Load the FXML and get the root node (the tile)
                        Node tileNode = loader.load();

                        // Access the controller for the loaded tile
                        ClassroomTileStudentController tileController = loader.getController();

                        // Initialize the tile with classroom data
                        String classroomCode = resultSet.getString("classroom_code");
                        String subjectName = resultSet.getString("subject_name");
                        int classroomId = resultSet.getInt("classroom_id");
                        tileController.initializeTile(classroomCode, subjectName, classroomId);

                        // Add the loaded tile to the TilePane
                        classroomTilePane.getChildren().add(tileNode);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Close the result set and statement
                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
