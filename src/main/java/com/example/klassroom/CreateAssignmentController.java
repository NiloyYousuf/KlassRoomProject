
package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private String selectedFileName;


    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            selectedFileName = selectedFile.getName();
            selectedFileLabel.setText("Selected File: " + selectedFileName);
        }
    }

    public void postAssignment() {
        LocalDate assignDateValue = assignDate.getValue();
        LocalDate deadlineValue = deadline.getValue();
        String marksValue = marks.getText();
        String text = assignmentText.getText();

        try {
            Connection connection = DatabaseConnection.getConnection();
            String insertQuery = "INSERT INTO assignments (Assignment_Text, Assign_Date, Deadline, Attachment, Original_Filename, Marks, Teacher_Username, Classroom_Code) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, text);
            preparedStatement.setDate(2, Date.valueOf(assignDateValue));
            preparedStatement.setDate(3, Date.valueOf(deadlineValue));

            if (selectedFile != null) {
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                preparedStatement.setBytes(4, fileContent);
                preparedStatement.setString(5, selectedFileName); // Set the original filename
            } else {
                preparedStatement.setNull(4, Types.BLOB);
                preparedStatement.setNull(5, Types.VARCHAR);
            }

            preparedStatement.setInt(6, Integer.parseInt(marksValue));
            preparedStatement.setString(7, CurrentTeacher.current_teacher_username); // Replace with actual teacher username
            preparedStatement.setString(8, CurrentClassroom.classroomCode); // Replace with actual classroom code

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

    public void back_button_clicked() throws IOException {
        GlobalFxmlString.FXML_to_load="classroomTeacher.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TeacherFinalDashboard.fxml"));
        Parent root = loader.load();

        // Get the current stage (assuming you have a reference to the current stage)
        Stage stage = (Stage) assignDate.getScene().getWindow();

        // Set the new FXML content on the current stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

