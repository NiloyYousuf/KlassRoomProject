package com.example.klassroom;

import com.example.StudentMonthlyAttendanceController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

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
    private TableColumn<AttendanceRecord, String> studentUsernameColumn;

    @FXML
    private TableColumn<AttendanceRecord, String> dateColumn;

    @FXML
    private TableColumn<AttendanceRecord, Boolean> statusColumn;

    @FXML
    private TextField studentUsernameInput;

    @FXML
    private CheckBox statusInput;

    @FXML
    private Label errorLabel;

    // Initialize the table and data here
    private ObservableList<AttendanceRecord> attendanceRecords = FXCollections.observableArrayList();

    public void initialize() {
        studentUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("studentUsername"));
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

    @FXML
    private void markAttendance() {
        String studentUsername = studentUsernameInput.getText();
        boolean isStatusSelected = statusInput.isSelected();
        LocalDate selectedDate = datePicker.getValue();

        if (!studentUsername.isEmpty()) {
            if (selectedDate != null) {
                boolean duplicateEntry = false;
                for (AttendanceRecord existingRecord : attendanceRecords) {
                    if (existingRecord.getStudentUsername().equals(studentUsername) &&
                            existingRecord.getDate().equals(selectedDate.toString())) {
                        duplicateEntry = true;
                        break;
                    }
                }

                if (!duplicateEntry) {
                    AttendanceRecord record = new AttendanceRecord(studentUsername, selectedDate.toString(), isStatusSelected);
                    attendanceRecords.add(record);
                    insertAttendanceToDatabase(studentUsername, selectedDate, isStatusSelected);
                    errorLabel.setText("");
                } else {
                    errorLabel.setText("Duplicate entry for Student Username and Date.");
                }
            } else {
                errorLabel.setText("Please select a date.");
            }
        } else {
            errorLabel.setText("Please enter a Student Username.");
        }
    }

    // Other methods for generating and displaying monthly attendance data

    private ObservableList<AttendanceRecord> fetchAttendanceDataFromDatabase(LocalDate selectedDate) {
        ObservableList<AttendanceRecord> data = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "200041123")) {
            String sql = "SELECT student_username, is_present FROM attendance WHERE Date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, java.sql.Date.valueOf(selectedDate));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String studentUsername = resultSet.getString("student_username");
                boolean isPresent = resultSet.getBoolean("is_present");
                data.add(new AttendanceRecord(studentUsername, selectedDate.toString(), isPresent));
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

    private void insertAttendanceToDatabase(String studentUsername, LocalDate date, boolean isPresent) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "200041123")) {
            String sql = "INSERT INTO attendance (student_username, Date, is_present) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentUsername);
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));
            preparedStatement.setBoolean(3, isPresent);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Error: Unable to insert attendance record.");
        }
    }

    public void generateMonthlyAttendance(ActionEvent event) {

        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            int year = selectedDate.getYear();
            int month = selectedDate.getMonthValue();

            new StudentMonthlyAttendanceController(year,month);
        }
    }

    public static class AttendanceRecord {
        private final String studentUsername;
        private final String date;
        private final boolean status;

        public AttendanceRecord(String studentUsername, String date, boolean status) {
            this.studentUsername = studentUsername;
            this.date = date;
            this.status = status;
        }

        public String getStudentUsername() {
            return studentUsername;
        }

        public String getDate() {
            return date;
        }

        public boolean isStatus() {
            return status;
        }
    }
}
