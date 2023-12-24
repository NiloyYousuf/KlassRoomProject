package com.example.teacher;

import com.example.klassroom.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
        TeacherMenuController.teacher_username = CurrentTeacher.current_teacher_username;
        loadClassroomTiles();
    }

    private void loadClassroomTiles() {
        classroomTilePane.getChildren().clear();
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            try {
                String sql = "SELECT classroom_code, subject_name, classroom_id,classroom_password FROM classrooms WHERE teacher_username = ?";
                DatabaseConnection.establishConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, teacher_username);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/klassroom/ClassroomTileTeacher.fxml").toURL());
                    try {
                        Node tileNode = loader.load();
                        ClassroomTileTeacherController tileController = loader.getController();
                        String classroomCode = resultSet.getString("classroom_code");
                        String subjectName = resultSet.getString("subject_name");
                        String classroomPassword=resultSet.getString("classroom_password");
                        int classroomId = resultSet.getInt("classroom_id");
                        tileController.initializeTile(classroomCode, subjectName, classroomId,classroomPassword);
                        tileController.setTeacherMenuController(this);
                        classroomTilePane.getChildren().add(tileNode);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void refreshClassroomTiles() {
        loadClassroomTiles();
    }
}
