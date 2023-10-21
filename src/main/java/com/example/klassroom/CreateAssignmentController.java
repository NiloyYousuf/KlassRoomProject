
package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.nio.file.Files;

public class CreateAssignmentController {

    @FXML
    private DatePicker assignDate;
    @FXML
    private DatePicker deadline;
    @FXML
    private TextField marks;
    @FXML
    private TextArea assignmentText;
    @FXML
    private Label selectedFileLabel;

    private File selectedFile;

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            selectedFile = file;
            selectedFileLabel.setText("Selected File: " + selectedFile.getName());
        }
    }

    public void postAssignment() {
        LocalDate assignDateValue = assignDate.getValue();
        LocalDate deadlineValue = deadline.getValue();
        String marksValue = marks.getText();
        String text = assignmentText.getText();

        try {
            Connection connection = DatabaseConnection.getConnection();
            String insertQuery = "INSERT INTO assignments (Assignment_Text, Assign_Date, Deadline, Attachment, Marks, Teacher_Username, Classroom_Code) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, text);
            preparedStatement.setDate(2, Date.valueOf(assignDateValue));
            preparedStatement.setDate(3, Date.valueOf(deadlineValue));

            if (selectedFile != null) {
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                preparedStatement.setBytes(4, fileContent);
            } else {
                preparedStatement.setNull(4, Types.BLOB);
            }

            preparedStatement.setInt(5, Integer.parseInt(marksValue));
            preparedStatement.setString(6, CurrentTeacher.current_teacher_username); // Replace with actual teacher username
            preparedStatement.setString(7, CurrentClassroom.classroomCode); // Replace with actual classroom code

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            if (rowsAffected > 0) {
                // Assignment posted successfully, display a success message or perform any other actions
                closeForm();
            } else {
                // Handle database insert failure, display an error message, or take appropriate action
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Handle database errors or file reading errors
        }
    }

    private void closeForm() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Assignment Upload");
        alert.setHeaderText(null);
        alert.setContentText("Assignment uploaded successfully!");

        alert.showAndWait();

    }
}
