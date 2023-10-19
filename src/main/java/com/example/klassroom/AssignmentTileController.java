package com.example.klassroom;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AssignmentTileController {
    @FXML
    private Label assignmentIdLabel;
    @FXML
    private Label assignmentTextLabel;
    @FXML
    private Label assignDateLabel;
    @FXML
    private Label deadlineLabel;


    @FXML
    private  Label classroomCode;

    @FXML
    private  Label status;

    @FXML
    private Button see_assignment;

    public void setAssignmentData(String assignmentText, String assignDate, String deadline) {
        assignmentTextLabel.setText("Assignment Text: " + assignmentText);
        assignDateLabel.setText("Assign Date: " + assignDate);
        deadlineLabel.setText("Deadline: " + deadline);

    }


    public void setAssignmentData(String assignmentText, String assignDate, String deadline, int assignment_ID, String assignment_submission_status, String assignment_classroomcode) {
        assignmentTextLabel.setText("Assignment Text: " + assignmentText);
        assignDateLabel.setText("Assign Date: " + assignDate);
        deadlineLabel.setText("Deadline: " + deadline);
      assignmentIdLabel.setText("Assignment_ID: "+ assignment_ID);
      classroomCode.setText("Classroom Code: " +assignment_classroomcode);
      status.setText("Submission Status : " + assignment_submission_status);



    }

    public void see_assignment_button_pressed()
    {

    }
}
