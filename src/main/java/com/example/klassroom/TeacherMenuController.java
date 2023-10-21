package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherMenuController {
    @FXML
    private ScrollPane classroomScrollPane;

    @FXML
    private TilePane classroomTilePane;

    // The teacher's ID; you should set this value when the teacher logs in
    public static String teacher_username;

    @FXML
    public void initialize() {
        TeacherMenuController.teacher_username=CurrentTeacher.current_teacher_username;
        // Load and populate the classroom tiles for the logged-in teacher
        loadClassroomTiles();
    }


    // This method loads and populates the classroom tiles
    private void loadClassroomTiles() {
        // Clear any existing tiles
        classroomTilePane.getChildren().clear();

        // Fetch classroom data from the database based on the teacher's ID
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            try {
                // Define the SQL query to retrieve classroom information for the teacher
                String sql = "SELECT classroom_code, subject_name,classroom_id FROM classrooms WHERE teacher_username = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, teacher_username);
                System.out.println(teacher_username);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    // Load the custom tile FXML for each classroom

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassroomTileTeacher.fxml"));
                    try {
                        // Load the FXML and get the root node (the tile)
                        Node tileNode = loader.load();

                        // Access the controller for the loaded tile
                        ClassroomTileTeacherController tileController = loader.getController();

                        // Initialize the tile with classroom data
                        String classroomCode = resultSet.getString("classroom_code");
                        String subjectName = resultSet.getString("subject_name");
                        int classroomid=resultSet.getInt("classroom_id");
                        tileController.initializeTile(classroomCode, subjectName,classroomid);

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
