package com.example.student;

//import com.sun.javafx.stage.EmbeddedWindow;
import com.example.Current_Variables.CurrentClassroom;
import com.example.klassroom.GlobalFxmlString;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;

public class ClassroomTileStudentController {
    @FXML
    private Label classroomLabel;
    @FXML
    private Label classroomCodeLabel;
    @FXML
    private Label subjectLabel;

    int classroomId;

    public void initializeTile(String classroomCode, String subjectName,int classroomId) {
        classroomCodeLabel.setText(classroomCode);
        subjectLabel.setText(subjectName);
        this.classroomId=classroomId;
        // You can customize the tile further if needed
    }


    @FXML
    private void tileClicked() throws IOException {
        // Load the student login FXML file
        GlobalFxmlString.FXML_to_load="src/main/resources/com/example/klassroom/classroomStudent.fxml";
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/StudentFinalDashboard.fxml").toURL());
        Parent studentLogin = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene = classroomLabel.getScene();
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
}
