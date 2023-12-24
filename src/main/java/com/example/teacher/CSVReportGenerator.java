package com.example.teacher;

import com.example.klassroom.DatabaseConnection;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVReportGenerator {

    public static void generateCSVReport(String classroomCode, Stage primaryStage) {
        try {
            // Query the database to get attendance data
            Map<String, List<AttendanceRecord>> attendanceRecords = getAttendanceRecordsFromDatabase(classroomCode);

            // Show file chooser to select the location to save the CSV file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV Report");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                // Write data to the selected CSV file
                writeDataToCSV(attendanceRecords, file);

                System.out.println("CSV report generated successfully.");
            } else {
                System.out.println("CSV report generation canceled.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to generate CSV report.");
        }
    }

    private static Map<String, List<AttendanceRecord>> getAttendanceRecordsFromDatabase(String classroomCode) {
        Map<String, List<AttendanceRecord>> attendanceRecords = new HashMap<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT student_username, Date, is_present FROM attendance WHERE classroom_code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, classroomCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String studentUsername = resultSet.getString("student_username");
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                boolean isPresent = resultSet.getBoolean("is_present");

                attendanceRecords
                        .computeIfAbsent(studentUsername, k -> new ArrayList<>())
                        .add(new AttendanceRecord(date, isPresent));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceRecords;
    }



// ...

    private static void writeDataToCSV(Map<String, List<AttendanceRecord>> attendanceRecords, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Write header
            writer.append("Student ID");

            // Collect all unique dates from attendance records
            List<LocalDate> allDates = attendanceRecords.values()
                    .stream()
                    .flatMap(List::stream)
                    .map(AttendanceRecord::getDate)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            // Write dates as text in the header
            for (LocalDate date : allDates) {
                writer.append(",").append(" " + date.toString());
            }
            writer.append("\n");

            // Write data
            for (Map.Entry<String, List<AttendanceRecord>> entry : attendanceRecords.entrySet()) {
                writer.append(entry.getKey());

                // Iterate through all dates
                for (LocalDate date : allDates) {
                    // Check if the student has attendance record for the current date
                    boolean isPresent = entry.getValue()
                            .stream()
                            .anyMatch(record -> record.getDate().equals(date) && record.isPresent());

                    writer.append(",").append(isPresent ? "P" : "A");
                }

                writer.append("\n");
            }
        }
    }




    public static class AttendanceRecord {
        private final LocalDate date;
        private final boolean isPresent;

        public AttendanceRecord(LocalDate date, boolean isPresent) {
            this.date = date;
            this.isPresent = isPresent;
        }

        public LocalDate getDate() {
            return date;
        }

        public boolean isPresent() {
            return isPresent;
        }
    }
}
