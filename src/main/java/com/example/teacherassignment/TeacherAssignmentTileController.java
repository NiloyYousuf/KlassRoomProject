package com.example.teacherassignment;

import com.example.Current_Variables.current_Assignment;
import com.example.klassroom.GlobalFxmlString;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;

public class  TeacherAssignmentTileController {
    @FXML
    private Text assignmentText;

    @FXML
    private Text assignDate;

    @FXML
    private Text deadline;

    @FXML
    private Text marks;

    @FXML
    private Text classroomCode;

    @FXML
    private Text Assignment_ID;

    public void setAssignmentDetails(String text, String date, String dueDate, String assignmentMarks, String code, int Assignemnt_ID) {
        assignmentText.setText(text);
        assignDate.setText(date);
        deadline.setText(dueDate);
        marks.setText(assignmentMarks);
        classroomCode.setText(code);
        Assignment_ID.setText(Integer.toString(Assignemnt_ID));
    }


    public void view_submissions_buttonclicked() throws IOException {
        current_Assignment.current_assignment_ID=Integer.parseInt(Assignment_ID.getText());
        //System.out.println("Current Assignment ID : " + current_Assignment.current_assignment_ID);
        GlobalFxmlString.FXML_to_load="src/main/resources/com/example/StudentAssignmentSubmissions/AssignmentInfo.fxml";
        FXMLLoader loader = new FXMLLoader(new File ("src/main/resources/com/example/Dashboards/TeacherFinalDashboard.fxml").toURL());
        Parent studentLogin = loader.load();

        // Get the current scene and set the student login content
        Scene currentScene = assignDate.getScene();
        currentScene.setRoot(studentLogin);

    }
}

