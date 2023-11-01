package com.example.student;

import com.example.Current_Variables.CurrentClassroom;
import com.example.klassroom.DatabaseConnection;
import com.example.klassroom.GlobalFxmlString;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class StudentAttendanceController {

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<AttendanceRecord> attendanceTable;

    @FXML
    private TableColumn<AttendanceRecord, LocalDate> dateColumn;

    @FXML
    private TableColumn<AttendanceRecord, Boolean> statusColumn;

    @FXML
    private Label errorLabel;

    // Initialize the table and data here
    private ObservableList<AttendanceRecord> attendanceRecords = FXCollections.observableArrayList();

    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        attendanceTable.setItems(attendanceRecords);
    }

    @FXML
    private void loadAttendanceData() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            ObservableList<AttendanceRecord> data = fetchAttendanceDataFromDatabase(selectedDate);
            displayAttendanceData(data);
        }
    }

    // Other methods for displaying monthly attendance data

    private ObservableList<AttendanceRecord> fetchAttendanceDataFromDatabase(LocalDate selectedDate) {
        ObservableList<AttendanceRecord> data = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String classroomCode = CurrentClassroom.classroomCode;
            String studentUsername =  CurrentStudent.CurrentStudentUsername;
            System.out.println("Finding Attendance record for classroom and student" + studentUsername + classroomCode);
            // Extract month and year from the selected date
            int selectedMonth = selectedDate.getMonthValue();
            int selectedYear = selectedDate.getYear();

            String sql = "SELECT Date, is_present FROM attendance " +
                    "WHERE classroom_code = ? AND student_username = ? " +
                    "AND MONTH(Date) = ? AND YEAR(Date) = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, classroomCode);
            preparedStatement.setString(2, studentUsername);
            preparedStatement.setInt(3, selectedMonth);
            preparedStatement.setInt(4, selectedYear);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Date date = resultSet.getDate("Date");
                boolean isPresent = resultSet.getBoolean("is_present");
                data.add(new AttendanceRecord(date.toLocalDate(), isPresent));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    private void displayAttendanceData(ObservableList<AttendanceRecord> data) {
        attendanceTable.getItems().clear();
        attendanceTable.getItems().addAll(data);
    }

    public static class AttendanceRecord {
        private final LocalDate date;
        private final boolean status;

        public AttendanceRecord(LocalDate date, boolean status) {
            this.date = date;
            this.status = status;
        }

        public LocalDate getDate() {
            return date;
        }

        public boolean isStatus() {
            return status;
        }
    }


    public void backbutton_clicked() throws IOException {

        GlobalFxmlString.FXML_to_load="src/main/resources/com/example/klassroom/classroomStudent.fxml";
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/StudentFinalDashboard.fxml").toURL());
        Parent root = loader.load();

        // Get the current stage (assuming you have a reference to the current stage)
        Stage stage = (Stage)errorLabel.getScene().getWindow();

        // Set the new FXML content on the current stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
