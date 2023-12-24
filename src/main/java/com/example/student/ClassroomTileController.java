package com.example.student;

//import com.sun.javafx.stage.EmbeddedWindow;
import com.example.Current_Variables.CurrentClassroom;
import com.example.teacher.TeacherMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

public class ClassroomTileController {
    @FXML
    private Label classroomLabel;
    @FXML
    private Label classroomCodeLabel;
    @FXML
    private Label subjectLabel;

    private String classroomcode;
    private  String subject_name;

    int classroomId;

    public void initializeTile(String classroomCode, String subjectName,int classroomId) {

        classroomCodeLabel.setText(classroomCode);
        subjectLabel.setText(subjectName);
        this.classroomId=classroomId;
        this.classroomcode=classroomCode;
        this.subject_name=subjectName;
        // You can customize the tile further if needed
    }


    @FXML
    private void tileClicked() throws IOException {
        // Load the student login FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("classroomTeacher.fxml"));
        Parent studentLogin = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene = classroomLabel.getScene();
        CurrentClassroom.setClassroomCode(classroomcode);
        CurrentClassroom.setSubjectName(subject_name);
        CurrentClassroom.setTeacherUsername(TeacherMenuController.teacher_username);
        CurrentClassroom.setClassroomId(classroomId);
        try {

            currentScene.setRoot(studentLogin);
        }
        catch (Exception e)
        {
          //  System.out.println("scene");
        }

    }
}
