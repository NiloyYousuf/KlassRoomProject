package com.example.student;

import com.example.Current_Variables.CurrentClassroom;
import com.example.klassroom.DatabaseConnection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StudentsMonthlyAttendanceChart extends JPanel {

    private String classroomCode;
    private JFrame frame;

    public StudentsMonthlyAttendanceChart(String classroomCode) {
        this.classroomCode = classroomCode;

        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        add(chartPanel);

        // Remove the following lines from the constructor:
        // JFrame frame = new JFrame("Monthly Classroom Attendance Chart");
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // frame.getContentPane().add(this);
        // frame.pack();
        // frame.setVisible(true);

        // Initialize the frame once in the constructor
        frame = new JFrame("Monthly Classroom Attendance Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this);
    }


    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Fetch monthly attendance data for all students in the classroom from the database
        Map<String, Integer> attendanceData = fetchMonthlyAttendanceData();
        for (Map.Entry<String, Integer> entry : attendanceData.entrySet()) {
            dataset.addValue(entry.getValue(), "Attendance", entry.getKey());
        }
        return dataset;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createBarChart(
                " Classroom Attendance",
                "Student",
                "Attendance",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    public void generateMonthlyAttendanceChart() {
        // Remove the following lines from the generateMonthlyAttendanceChart method:
        // JFrame frame = new JFrame("Monthly Classroom Attendance Chart");
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // frame.getContentPane().add(this);
        // frame.pack();
        // frame.setVisible(true);

        // Use the existing frame for showing the chart
        frame.pack();
        frame.setVisible(true);
    }

    private Map<String, Integer> fetchMonthlyAttendanceData() {
        Map<String, Integer> attendanceData = new HashMap<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT student_username, SUM(is_present) AS attendance_count " +
                    "FROM attendance " +
                    "WHERE classroom_code = ? " +
                    "GROUP BY student_username";
            DatabaseConnection.establishConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, CurrentClassroom.classroomCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String studentUsername = resultSet.getString("student_username");
                int attendanceCount = resultSet.getInt("attendance_count");
                attendanceData.put(studentUsername, attendanceCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceData;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new StudentsMonthlyAttendanceChart("C101", 2023, 9).generateMonthlyAttendanceChart();
//        });
//    }
}
