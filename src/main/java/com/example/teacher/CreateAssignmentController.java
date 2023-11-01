
package com.example.teacher;

import com.example.Current_Variables.CurrentClassroom;
import com.example.klassroom.DatabaseConnection;
import com.example.klassroom.GlobalFxmlString;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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

    @FXML
    public void initialize() {
        // This method will be called after the FXML file has been loaded
        try {
            viewexistingassignemnt();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception if necessary
        }
    }
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

        // Validate Assign Date
        if (assignDateValue == null) {
            assignDateValue = LocalDate.now(); // Use system date if Assign Date is not chosen
        }

        // Validate Text
        if (text.trim().isEmpty()) {
            // Show an alert and return without posting the assignment
            showAlert("Error", "Assignment text cannot be empty.");
            return;
        }

        // Validate Marks
        if (marksValue.trim().isEmpty() || !marksValue.matches("\\d+")) {
            // Show an alert and return without posting the assignment
            showAlert("Error", "Please enter a valid numeric value for marks.");
            return;
        }

        try {
            Connection connection = DatabaseConnection.getConnection();
            String insertQuery = "INSERT INTO assignments (Assignment_Text, Assign_Date, Deadline, Attachment, Original_Filename, Marks, Teacher_Username, Classroom_Code) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, text);
            preparedStatement.setDate(2, Date.valueOf(assignDateValue));

            // Validate Deadline
            if (deadlineValue != null) {
                preparedStatement.setDate(3, Date.valueOf(deadlineValue));
            } else {
                preparedStatement.setNull(3, Types.DATE); // Set as NULL if no deadline is chosen
            }

            if (selectedFile != null) {
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                preparedStatement.setBytes(4, fileContent);
                preparedStatement.setString(5, selectedFileName); // Set the original filename
            } else {
                preparedStatement.setNull(4, Types.BLOB);
                preparedStatement.setNull(5, Types.VARCHAR);
            }

            preparedStatement.setInt(6, Integer.parseInt(marksValue));
            preparedStatement.setString(7, CurrentTeacher.current_teacher_username);
            preparedStatement.setString(8, CurrentClassroom.classroomCode);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            if (rowsAffected > 0) {
                // Assignment posted successfully, display a success message or perform any other actions

                viewexistingassignemnt();
                closeForm();
            } else {
                // Handle database insert failure, display an error message, or take appropriate action
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Handle database errors or file reading errors
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }




    private void closeForm() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Assignment Upload");
        alert.setHeaderText(null);
        alert.setContentText("Assignment uploaded successfully!");

        alert.showAndWait();

    }

    public void back_button_clicked() throws IOException {
        GlobalFxmlString.FXML_to_load="src/main/resources/com/example/klassroom/classroomTeacher.fxml";
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/TeacherFinalDashboard.fxml").toURL());
        Parent root = loader.load();

        // Get the current stage (assuming you have a reference to the current stage)
        Stage stage = (Stage) assignDate.getScene().getWindow();

        // Set the new FXML content on the current stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

//    public void viewexistingassignemnt() throws IOException {
//        GlobalFxmlString.FXML_to_load="src/main/resources/com/example/teacherassignment/TeacherFetchAssignment.fxml";
//        FXMLLoader loader = new FXMLLoader(new File ("src/main/resources/com/example/Dashboards/TeacherFinalDashboard.fxml").toURL());
//        Parent root = loader.load();
//        // Get the current stage (assuming you have a reference to the current stage)
//        Stage stage = (Stage) assignDate.getScene().getWindow();
//
//        // Set the new FXML content on the current stage
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

    @FXML
    private VBox dynamicContentVBox;
        public void viewexistingassignemnt() throws IOException {
            FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/teacherassignment/TeacherFetchAssignment.fxml").toURL());
            VBox teacherFetchAssignment = loader.load();

            // Clear existing content and add the loaded dynamic content to the VBox
            dynamicContentVBox.getChildren().clear();
            dynamicContentVBox.getChildren().add(teacherFetchAssignment);
    }

}

