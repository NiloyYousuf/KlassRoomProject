package com.example.student;

import com.example.klassroom.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentAssignmentsAppController {

    @FXML
    private VBox    assignmentTilesContainer ;
    private List<String> classroomCodes = new ArrayList<>(); // Declare and initialize classroomCodes


    @FXML
    private ComboBox<String> statusFilterComboBox;

    public void initialize() {
        // Load assignment tiles based on the student's ID

        statusFilterComboBox.getItems().addAll("All", "Submitted", "Marked", "Missed", "Due");

        // Set the default selection to "All"
        statusFilterComboBox.getSelectionModel().select("All");

        // Set up an event handler for the ComboBox selection change
        statusFilterComboBox.setOnAction(event -> filterAssignmentsByStatus());
        fetchClassroomCodes(); // Fetch classroom codes
        loadAssignmentTiles();
    }



    private void fetchClassroomCodes() {
        // Connect to the database and retrieve classroom codes for the student
        try {
            Connection connection = DatabaseConnection.getConnection();
            DatabaseConnection.establishConnection();
            String classroomQuery = "SELECT classroom_code FROM classroom_student_junction WHERE student_username = ?";
            PreparedStatement classroomStatement;
            classroomStatement = connection.prepareStatement(classroomQuery);
            classroomStatement.setString(1, CurrentStudent.CurrentStudentUsername);
            ResultSet classroomResultSet = classroomStatement.executeQuery();

            while (classroomResultSet.next()) {
                classroomCodes.add(classroomResultSet.getString("classroom_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database-related exceptions
        }

    }

    private void loadAssignmentTiles() {
        // Create a list to store classroom codes for the student
        List<String> classroomCodes = new ArrayList<>();

        // Connect to the database and retrieve classroom codes for the student
        try {Connection connection = DatabaseConnection.getConnection();
            DatabaseConnection.establishConnection();
            String classroomQuery = "SELECT classroom_code FROM classroom_student_junction WHERE student_username = ?";
            PreparedStatement classroomStatement = connection.prepareStatement(classroomQuery);
            classroomStatement.setString(1, CurrentStudent.CurrentStudentUsername);
            ResultSet classroomResultSet = classroomStatement.executeQuery();

            while (classroomResultSet.next()) {
                classroomCodes.add(classroomResultSet.getString("classroom_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database-related exceptions
        }

        // Now, you have a list of classroom codes that the student is enrolled in
        // Use this list to fetch assignments for those classrooms
        for (String classroomCode : classroomCodes) {
            try {Connection connection = DatabaseConnection.getConnection();
                DatabaseConnection.establishConnection();
                String sql = "SELECT Assignment_ID, Assignment_Text, Assign_Date, Deadline, Attachment, Marks, Teacher_Username, Classroom_Code FROM assignments WHERE Classroom_Code = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, classroomCode);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Assignment/AssignmentTile.fxml").toURL());
                    Parent assignmentTile = loader.load();
                    AssignmentTileController controller = loader.getController();
                    int assignment_ID = resultSet.getInt("Assignment_ID");
                    String assignmentText = resultSet.getString("Assignment_Text");
                    String assignDate = resultSet.getString("Assign_Date");
                    String deadline = resultSet.getString("Deadline");

                    controller.setAssignmentData(assignmentText, assignDate, deadline, assignment_ID, classroomCode);

                    assignmentTilesContainer.getChildren().add(assignmentTile);
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                // Handle any database-related exceptions
            }
        }
    }


    private void filterAssignmentsByStatus() {
        String selectedStatus = statusFilterComboBox.getValue();

        // Clear existing tiles
        assignmentTilesContainer.getChildren().clear();

        // Load assignment tiles based on the selected status
        if ("All".equals(selectedStatus)) {
            loadAssignmentTiles();
        } else {
            for (String classroomCode : classroomCodes) {
                try  {
                    // ... (unchanged)
                    Connection connection = DatabaseConnection.getConnection();
                    DatabaseConnection.establishConnection();
                    String sql;
                    PreparedStatement preparedStatement;

                    switch (selectedStatus) {
                        case "Submitted":
                            sql = "SELECT a.Assignment_ID, a.Assignment_Text, a.Assign_Date, a.Deadline, a.Attachment, a.Marks, a.Teacher_Username, a.Classroom_Code " +
                                    "FROM assignments a " +
                                    "JOIN student_assignment_junction saj ON a.Assignment_ID = saj.Assignment_ID " +
                                    "WHERE a.Classroom_Code = ? AND saj.Student_Username = ?";
                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, classroomCode);
                            preparedStatement.setString(2, CurrentStudent.CurrentStudentUsername);
                            break;
                        case "Marked":
                        sql = "SELECT a.Assignment_ID, a.Assignment_Text, a.Assign_Date, a.Deadline, a.Attachment, saj.Marks_Obtained as Marks, a.Teacher_Username, a.Classroom_Code " +
                                "FROM assignments a " +
                                "JOIN student_assignment_junction saj ON a.Assignment_ID = saj.Assignment_ID " +
                                "WHERE a.Classroom_Code = ? AND saj.Student_Username = ? AND saj.Submission_Status = 'Marked'";
                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, classroomCode);
                            preparedStatement.setString(2, CurrentStudent.CurrentStudentUsername);
                            break;
                        case "Missed":
                            sql = "SELECT Assignment_ID, Assignment_Text, Assign_Date, Deadline, Attachment, Marks, Teacher_Username, Classroom_Code " +
                                    "FROM assignments " +
                                    "WHERE Classroom_Code = ? AND Assignment_ID NOT IN " +
                                    "(SELECT Assignment_ID FROM student_assignment_junction WHERE Student_Username = ?) AND Deadline < CURRENT_DATE";
                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, classroomCode);
                            preparedStatement.setString(2, CurrentStudent.CurrentStudentUsername);
                            break;
                        case "Due":
                            sql =  "SELECT Assignment_ID, Assignment_Text, Assign_Date, Deadline, Attachment, Marks, Teacher_Username, Classroom_Code " +
                                    "FROM assignments a " +
                                    "WHERE Classroom_Code = ? AND Deadline >= CURRENT_DATE AND " +
                                    "NOT EXISTS (SELECT 1 FROM student_assignment_junction saj " +
                                    "WHERE saj.Assignment_ID = a.Assignment_ID " +
                                    "AND saj.Student_Username = ? " +
                                    "AND (saj.Submission_Status = 'Submitted' OR saj.Submission_Status = 'Marked'))";
                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, classroomCode);
                            preparedStatement.setString(2, CurrentStudent.CurrentStudentUsername);
                            break;

                        default:
                            // Handle unknown status
                            return;
                    }

                    ResultSet resultSet2 = preparedStatement.executeQuery();

                    while (resultSet2.next()) {
                        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Assignment/AssignmentTile.fxml").toURL());
                        Parent assignmentTile = loader.load();
                        AssignmentTileController controller = loader.getController();
                        int assignment_ID = resultSet2.getInt("Assignment_ID");
                        String assignmentText = resultSet2.getString("Assignment_Text");
                        String assignDate = resultSet2.getString("Assign_Date");
                        String deadline = resultSet2.getString("Deadline");
                        controller.setAssignmentData(assignmentText, assignDate, deadline, assignment_ID, classroomCode);

                        assignmentTilesContainer.getChildren().add(assignmentTile);
                    }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                    // Handle any database-related exceptions
                }
            }
        }
    }


}
