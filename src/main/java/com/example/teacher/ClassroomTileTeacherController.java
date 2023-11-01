package com.example.teacher;

//import com.sun.javafx.stage.EmbeddedWindow;
import com.example.Current_Variables.CurrentClassroom;
import com.example.klassroom.DatabaseConnection;
import com.example.klassroom.GlobalFxmlString;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClassroomTileTeacherController {

    @FXML
    private Label classroomCodeLabel;
    @FXML
    private Label subjectLabel;

    int classroomId;

    private TeacherMenuController teacherMenuController;

    public void initializeTile(String classroomCode, String subjectName,int classroomId) {
        classroomCodeLabel.setText(classroomCode);
        subjectLabel.setText(subjectName);
        this.classroomId=classroomId;
        // You can customize the tile further if needed
    }


    @FXML
    private void tileClicked() throws IOException {
        // Load the student login FXML file
        GlobalFxmlString.FXML_to_load="src/main/resources/com/example/klassroom/classroomTeacher.fxml";
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/TeacherFinalDashboard.fxml").toURL());
        Parent studentLogin = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene =classroomCodeLabel.getScene();
        CurrentClassroom.setClassroomCode(classroomCodeLabel.getText());
        CurrentClassroom.setSubjectName(subjectLabel.getText());
        CurrentClassroom.setClassroomId(classroomId);
        try {

            currentScene.setRoot(studentLogin);
        }
        catch (Exception e)
        {
            System.out.println("scene");
        }

    }


    @FXML
    private void deleteClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Classroom");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete this classroom?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // If 'OK' is pressed, proceed with deletion
                deleteClassroomFromDatabase();
            }
        });
    }

    private void deleteClassroomFromDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM classrooms WHERE classroom_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, classroomId);
            int deletedRows = preparedStatement.executeUpdate();

            if (deletedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Classroom deleted successfully.");
                teacherMenuController.refreshClassroomTiles();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the classroom.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setTeacherMenuController(TeacherMenuController teacherMenuController) {
        this.teacherMenuController = teacherMenuController;
    }

}
