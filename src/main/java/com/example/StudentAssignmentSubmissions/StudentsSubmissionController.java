package com.example.StudentAssignmentSubmissions;

import com.example.Current_Variables.CurrentClassroom;
import com.example.klassroom.DatabaseConnection;
import com.example.Current_Variables.current_Assignment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentsSubmissionController implements Initializable {

    @FXML
    private TilePane tilePane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This method is automatically called when the FXML is loaded

        // Call the displayAssignmentInfo method to populate the tiles
        displayAssignmentInfo();
    }

    private void displayAssignmentInfo() {
        String assignmentId = String.valueOf(current_Assignment.current_assignment_ID);

        tilePane.getChildren().clear(); // Clear existing tiles

        try {
            // Replace with your database connection details
            Connection connection = DatabaseConnection.getConnection();

            // SQL query to fetch student info and assignment data
            String sqlQuery = "SELECT saj.Student_Username, saj.Submission_Date, s.photo " +
                    "FROM student_assignment_junction saj " +
                    "INNER JOIN students s ON saj.Student_Username = s.student_username " +
                    "WHERE saj.Assignment_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, assignmentId);

            ResultSet resultSet = preparedStatement.executeQuery();

           if (resultSet.next()) {
                String studentUsername = resultSet.getString("Student_Username");
                String submissionDate = resultSet.getString("Submission_Date");
                byte[] photoBytes = resultSet.getBytes("photo");

                // Load the student tile FXML for each student
                FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentTile.fxml"));
                VBox studentTile = loader.load();

                // Get the controller for the student tile
                StudentTileController tileController = loader.getController();

                // Initialize the student tile controller with data
                tileController.initialize(studentUsername, submissionDate, photoBytes);

                // Add the populated student tile to the TilePane
                tilePane.getChildren().add(studentTile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void generateCSVReportButtonClicked() {
        // Show a file chooser to select the location to save the CSV file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        Stage stage = (Stage) tilePane.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // Generate the CSV report and save it to the selected location
            generateCSVReport(file);
        }
    }

    private void generateCSVReport(java.io.File file) {
        try (FileWriter writer = new FileWriter(file)) {
            // Write the CSV header
            writer.write("Student_Username,Marks_Obtained\n");

            // Fetch data from the database
            List<CSVData> csvDataList = fetchDataFromDatabase();

            // Write data to the CSV file
            for (CSVData data : csvDataList) {
                writer.write(data.getStudentUsername() + "," + data.getMarksObtained() + "\n");
            }

            System.out.println("CSV report generated successfully.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }
    private List<CSVData> fetchDataFromDatabase() throws SQLException {
        List<CSVData> csvDataList = new ArrayList<>();

        // Establish a database connection (assuming you have a DatabaseConnection class)
        Connection connection = DatabaseConnection.getConnection();

        // Use the current classroom code from the CurrentClassroom class
        String classroomCode = CurrentClassroom.classroomCode;
        int current_Assignment= com.example.Current_Variables.current_Assignment.current_assignment_ID;

        // SQL query to retrieve data from classroom_student_junction and student_assignment_junction using LEFT JOIN
        String sqlQuery = "SELECT csj.student_username, saj.Marks_Obtained " +
                "FROM classroom_student_junction csj " +
                "LEFT JOIN student_assignment_junction saj " +
                "ON csj.student_username = saj.Student_Username " +
                "WHERE csj.classroom_code = ? AND Assignment_ID=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, classroomCode);
            preparedStatement.setInt(2, current_Assignment);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CSVData csvData = new CSVData();
                csvData.setStudentUsername(resultSet.getString("student_username"));
                csvData.setMarksObtained(resultSet.getInt("Marks_Obtained"));

                csvDataList.add(csvData);
            }

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return csvDataList;
    }



    // Add a helper class for storing CSV data
    private static class CSVData {
        private String studentUsername;
        private int marksObtained;

        public String getStudentUsername() {
            return studentUsername;
        }

        public void setStudentUsername(String studentUsername) {
            this.studentUsername = studentUsername;
        }

        public int getMarksObtained() {
            return marksObtained;
        }

        public void setMarksObtained(int marksObtained) {
            this.marksObtained = marksObtained;
        }
    }
}
